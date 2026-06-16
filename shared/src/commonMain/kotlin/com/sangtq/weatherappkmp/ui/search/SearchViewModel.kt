package com.sangtq.weatherappkmp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangtq.weatherappkmp.data.storage.PreferencesStorage
import com.sangtq.weatherappkmp.domain.ForecastWeatherUseCase
import com.sangtq.weatherappkmp.domain.GetTimezoneUseCase
import com.sangtq.weatherappkmp.domain.SearchLocationUseCase
import com.sangtq.weatherappkmp.domain.model.FavoriteCity
import com.sangtq.weatherappkmp.domain.model.SearchLocation
import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchLocation: SearchLocationUseCase,
    private val getForecastWeather: ForecastWeatherUseCase,
    private val getTimezone: GetTimezoneUseCase,
    private val preferences: PreferencesStorage
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableStateFlow<Resource<List<SearchLocation>>>(Resource.Success(emptyList()))
    val searchResults: StateFlow<Resource<List<SearchLocation>>> = _searchResults

    private val _currentWeather = MutableStateFlow<WeatherData?>(null)
    val currentWeather: StateFlow<WeatherData?> = _currentWeather

    private val _timezones = MutableStateFlow<Map<Int, String>>(emptyMap())
    val timezones: StateFlow<Map<Int, String>> = _timezones

    val favorites: StateFlow<List<FavoriteCity>> = preferences.favorites
    val recent: StateFlow<List<String>> = preferences.recent

    init {
        loadCurrentWeather()
        observeSearchQuery()
    }

    private fun loadCurrentWeather() = viewModelScope.launch {
        getForecastWeather("Vietnam", 1).onSuccess { _currentWeather.value = it }
    }

    @Suppress("OPT_IN_USAGE")
    private fun observeSearchQuery() = viewModelScope.launch {
        _searchQuery
            .debounce(400)
            .distinctUntilChanged()
            .collect { query ->
                if (query.isBlank()) {
                    _searchResults.value = Resource.Success(emptyList())
                    _timezones.value = emptyMap()
                } else {
                    performSearch(query)
                }
            }
    }

    fun onQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun recordSelection(query: String) {
        preferences.addRecentSearch(query)
    }

    fun removeFavorite(city: FavoriteCity) {
        preferences.toggleFavorite(city)
    }

    fun clearRecent() {
        preferences.clearRecent()
    }

    private fun performSearch(query: String) = viewModelScope.launch {
        _searchResults.value = Resource.Loading
        searchLocation(query).fold(
            onSuccess = {
                _searchResults.value = Resource.Success(it)
                _timezones.value = emptyMap()
                fetchTimezones(it.take(5))
            },
            onFailure = { _searchResults.value = Resource.Error(it.message ?: "Unknown Error") }
        )
    }

    private fun fetchTimezones(locations: List<SearchLocation>) = viewModelScope.launch {
        val results = locations.map { loc ->
            async { loc.id to getTimezone("${loc.lat},${loc.lon}").getOrNull() }
        }.awaitAll()
        val map = _timezones.value.toMutableMap()
        results.forEach { (id, info) ->
            if (info != null) {
                val time = info.localtime.substringAfter(" ", info.localtime)
                map[id] = "$time · ${info.tzId}"
            }
        }
        _timezones.value = map
    }
}
