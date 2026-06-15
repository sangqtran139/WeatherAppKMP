package com.sangtq.weatherappkmp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangtq.weatherappkmp.domain.ForecastWeatherUseCase
import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherDetailViewModel(
    private val getForecastWeather: ForecastWeatherUseCase
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
}
