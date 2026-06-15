package com.sangtq.weatherappkmp.data.repository

import com.sangtq.weatherappkmp.data.mapper.toDomain
import com.sangtq.weatherappkmp.domain.model.SearchLocation
import com.sangtq.weatherappkmp.domain.repository.SearchRepository
import com.sangtq.weatherappkmp.network.WeatherApiClient

class SearchRepositoryImpl(
    private val apiClient: WeatherApiClient
) : SearchRepository {
    override suspend fun searchLocations(query: String): Result<List<SearchLocation>> =
        apiClient.searchLocations(query).map { list -> list.map { it.toDomain() } }
}
