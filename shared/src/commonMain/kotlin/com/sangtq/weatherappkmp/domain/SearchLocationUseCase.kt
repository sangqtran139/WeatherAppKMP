package com.sangtq.weatherappkmp.domain

import com.sangtq.weatherappkmp.domain.model.SearchLocation
import com.sangtq.weatherappkmp.domain.repository.SearchRepository

class SearchLocationUseCase(private val repository: SearchRepository) {
    suspend operator fun invoke(query: String): Result<List<SearchLocation>> {
        if (query.isBlank()) return Result.success(emptyList())
        return repository.searchLocations(query.trim())
    }
}
