package com.sangtq.weatherappkmp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangtq.weatherappkmp.domain.ForecastWeatherUseCase
import com.sangtq.weatherappkmp.domain.SearchLocationUseCase
import com.sangtq.weatherappkmp.domain.model.SearchLocation
import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchLocation: SearchLocationUseCase,
    private val getForecastWeather: ForecastWeatherUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableStateFlow<Resource<List<SearchLocation>>>(Resource.Success(emptyList()))
    val searchResults: StateFlow<Resource<List<SearchLocation>>> = _searchResults

    private val _currentWeather = MutableStateFlow<WeatherData?>(null)
    val currentWeather: StateFlow<WeatherData?> = _currentWeather

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
                } else {
                    performSearch(query)
                }
            }
    }

    fun onQueryChange(query: String) {
        _searchQuery.value = query
    }

    private fun performSearch(query: String) = viewModelScope.launch {
        _searchResults.value = Resource.Loading
        searchLocation(query).fold(
            onSuccess = { _searchResults.value = Resource.Success(it) },
            onFailure = { _searchResults.value = Resource.Error(it.message ?: "Unknown Error") }
        )
    }
}
