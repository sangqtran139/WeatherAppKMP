package com.sangtq.weatherappkmp.ui.astronomy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangtq.weatherappkmp.domain.GetAstronomyUseCase
import com.sangtq.weatherappkmp.domain.model.AstronomyDetail
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import com.sangtq.weatherappkmp.util.todayIsoDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AstronomyViewModel(
    private val getAstronomy: GetAstronomyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<AstronomyDetail>>(Resource.Loading)
    val uiState: StateFlow<Resource<AstronomyDetail>> = _uiState

    private val _selectedDate = MutableStateFlow(todayIsoDate())
    val selectedDate: StateFlow<String> = _selectedDate

    private var lastLocation: String = ""

    fun load(location: String, date: String = _selectedDate.value) {
        lastLocation = location
        _selectedDate.value = date
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            getAstronomy(location, date).fold(
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
}
