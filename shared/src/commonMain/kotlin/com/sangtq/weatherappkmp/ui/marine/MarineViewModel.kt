package com.sangtq.weatherappkmp.ui.marine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangtq.weatherappkmp.data.storage.PreferencesStorage
import com.sangtq.weatherappkmp.domain.GetMarineUseCase
import com.sangtq.weatherappkmp.domain.model.MarineForecast
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MarineViewModel(
    private val getMarine: GetMarineUseCase,
    private val preferences: PreferencesStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<MarineForecast>>(Resource.Loading)
    val uiState: StateFlow<Resource<MarineForecast>> = _uiState

    private val _selectedDayIndex = MutableStateFlow(0)
    val selectedDayIndex: StateFlow<Int> = _selectedDayIndex

    private var lastLocation: String = ""

    init {
        viewModelScope.launch {
            preferences.language.collect { _ ->
                if (lastLocation.isNotEmpty() && (_uiState.value as? Resource.Success) != null) {
                    load(lastLocation)
                }
            }
        }
    }

    fun load(location: String) {
        lastLocation = location
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            getMarine(location, language = preferences.language.value.code).fold(
                onSuccess = { _uiState.value = Resource.Success(it) },
                onFailure = { _uiState.value = Resource.Error(it.message ?: "Unknown Error") }
            )
        }
    }

    fun selectDay(index: Int) { _selectedDayIndex.value = index }

    fun retry() { if (lastLocation.isNotEmpty()) load(lastLocation) }
}
