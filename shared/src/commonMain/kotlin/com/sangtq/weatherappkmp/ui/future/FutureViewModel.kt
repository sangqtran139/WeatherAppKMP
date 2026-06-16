package com.sangtq.weatherappkmp.ui.future

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangtq.weatherappkmp.domain.GetFutureWeatherUseCase
import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import com.sangtq.weatherappkmp.util.addDaysToIsoDate
import com.sangtq.weatherappkmp.util.todayIsoDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FutureViewModel(
    private val getFuture: GetFutureWeatherUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<WeatherData>>(Resource.Loading)
    val uiState: StateFlow<Resource<WeatherData>> = _uiState

    // Default: 30 days ahead (valid range: 14..300)
    private val _selectedDate = MutableStateFlow(addDaysToIsoDate(todayIsoDate(), 30))
    val selectedDate: StateFlow<String> = _selectedDate

    private var lastLocation: String = ""

    fun load(location: String, date: String = _selectedDate.value) {
        lastLocation = location
        _selectedDate.value = date
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            getFuture(location, date).fold(
                onSuccess = { _uiState.value = Resource.Success(it) },
                onFailure = { _uiState.value = Resource.Error(it.message ?: "Unknown Error") }
            )
        }
    }

    fun setDate(date: String) {
        if (lastLocation.isNotEmpty() && date != _selectedDate.value) {
            load(lastLocation, date)
        }
    }

    fun shiftDays(days: Int) {
        val today = todayIsoDate()
        val minDate = addDaysToIsoDate(today, 14)
        val maxDate = addDaysToIsoDate(today, 300)
        val candidate = addDaysToIsoDate(_selectedDate.value, days)
        val clamped = when {
            candidate < minDate -> minDate
            candidate > maxDate -> maxDate
            else -> candidate
        }
        setDate(clamped)
    }
}
