package com.sangtq.weatherappkmp.ui.sports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangtq.weatherappkmp.domain.GetSportsUseCase
import com.sangtq.weatherappkmp.domain.model.SportCategory
import com.sangtq.weatherappkmp.domain.model.SportsData
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SportsViewModel(
    private val getSports: GetSportsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<SportsData>>(Resource.Loading)
    val uiState: StateFlow<Resource<SportsData>> = _uiState

    private val _selectedCategory = MutableStateFlow(SportCategory.FOOTBALL)
    val selectedCategory: StateFlow<SportCategory> = _selectedCategory

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    fun load(query: String) {
        _query.value = query
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            getSports(query).fold(
                onSuccess = { _uiState.value = Resource.Success(it) },
                onFailure = { _uiState.value = Resource.Error(it.message ?: "Unknown Error") }
            )
        }
    }

    fun selectCategory(category: SportCategory) {
        _selectedCategory.value = category
    }
}
