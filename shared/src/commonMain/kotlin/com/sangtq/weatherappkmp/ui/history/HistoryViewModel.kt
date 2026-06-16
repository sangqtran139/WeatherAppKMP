package com.sangtq.weatherappkmp.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangtq.weatherappkmp.domain.GetHistoryWeatherUseCase
import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import com.sangtq.weatherappkmp.util.addDaysToIsoDate
import com.sangtq.weatherappkmp.util.todayIsoDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val getHistory: GetHistoryWeatherUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<WeatherData>>(Resource.Loading)
    val uiState: StateFlow<Resource<WeatherData>> = _uiState

    // Default: 1 year ago today
    private val _selectedDate = MutableStateFlow(addDaysToIsoDate(todayIsoDate(), -365))
    val selectedDate: StateFlow<String> = _selectedDate

    private var lastLocation: String = ""

    fun load(location: String, date: String = _selectedDate.value) {
        lastLocation = location
        _selectedDate.value = date
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            getHistory(location, date).fold(
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
        val candidate = addDaysToIsoDate(_selectedDate.value, days)
        // History can't be in the future. Clamp to today.
        val clamped = if (candidate > today) today else candidate
        setDate(clamped)
    }
}
