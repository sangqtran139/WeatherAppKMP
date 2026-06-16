package com.sangtq.weatherappkmp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangtq.weatherappkmp.data.storage.PreferencesStorage
import com.sangtq.weatherappkmp.domain.ForecastWeatherUseCase
import com.sangtq.weatherappkmp.domain.GetLocationByIpUseCase
import com.sangtq.weatherappkmp.domain.model.FavoriteCity
import com.sangtq.weatherappkmp.domain.model.LocationResult
import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import com.sangtq.weatherappkmp.platform.LocationProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class WeatherHomeViewModel(
    private val getForecastWeather: ForecastWeatherUseCase,
    private val locationProvider: LocationProvider,
    private val getLocationByIp: GetLocationByIpUseCase,
    private val preferences: PreferencesStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<WeatherData>>(Resource.Loading)
    val uiState: StateFlow<Resource<WeatherData>> = _uiState

    val isCurrentFavorite: StateFlow<Boolean> = combine(_uiState, preferences.favorites) { state, favs ->
        val data = (state as? Resource.Success)?.data ?: return@combine false
        favs.any { it.query.equals(lastLocation, ignoreCase = true) || it.name.equals(data.location.name, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private var lastLocation = "Vietnam"

    fun loadWeather(location: String = lastLocation) = viewModelScope.launch {
        lastLocation = location
        _uiState.value = Resource.Loading
        getForecastWeather(location).fold(
            onSuccess = { _uiState.value = Resource.Success(it) },
            onFailure = { _uiState.value = Resource.Error(it.message ?: "Unknown Error") }
        )
    }

    fun detectAndLoadWeather(
        fallback: String = lastLocation,
        onLocationDetected: (String) -> Unit = {}
    ) = viewModelScope.launch {
        _uiState.value = Resource.Loading
        val location = when (val result = locationProvider.getCurrentLocation()) {
            is LocationResult.Success -> "${result.lat},${result.lon}".also { onLocationDetected(it) }
            is LocationResult.PermissionDenied, is LocationResult.Error -> resolveIpFallback(fallback, onLocationDetected)
        }
        lastLocation = location
        getForecastWeather(location).fold(
            onSuccess = { _uiState.value = Resource.Success(it) },
            onFailure = { _uiState.value = Resource.Error(it.message ?: "Unknown Error") }
        )
    }

    fun toggleFavorite() {
        val data = (_uiState.value as? Resource.Success)?.data ?: return
        preferences.toggleFavorite(
            FavoriteCity(
                name = data.location.name,
                country = data.location.country,
                query = lastLocation
            )
        )
    }

    private suspend fun resolveIpFallback(
        fallback: String,
        onLocationDetected: (String) -> Unit
    ): String = getLocationByIp().fold(
        onSuccess = { ip ->
            val coords = ip.coordinatesQuery
            if (coords == "0.0,0.0") fallback
            else coords.also { onLocationDetected(it) }
        },
        onFailure = { fallback }
    )
}
