package com.sangtq.weatherappkmp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangtq.weatherappkmp.domain.ForecastWeatherUseCase
import com.sangtq.weatherappkmp.domain.model.LocationResult
import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import com.sangtq.weatherappkmp.platform.LocationProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherHomeViewModel(
    private val getForecastWeather: ForecastWeatherUseCase,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<WeatherData>>(Resource.Loading)
    val uiState: StateFlow<Resource<WeatherData>> = _uiState

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
            is LocationResult.PermissionDenied, is LocationResult.Error -> fallback
        }
        lastLocation = location
        getForecastWeather(location).fold(
            onSuccess = { _uiState.value = Resource.Success(it) },
            onFailure = { _uiState.value = Resource.Error(it.message ?: "Unknown Error") }
        )
    }
}
