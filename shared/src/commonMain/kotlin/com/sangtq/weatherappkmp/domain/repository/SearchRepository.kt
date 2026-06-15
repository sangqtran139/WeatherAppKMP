package com.sangtq.weatherappkmp.domain.repository

import com.sangtq.weatherappkmp.domain.model.SearchLocation

interface SearchRepository {
    suspend fun searchLocations(query: String): Result<List<SearchLocation>>
}
