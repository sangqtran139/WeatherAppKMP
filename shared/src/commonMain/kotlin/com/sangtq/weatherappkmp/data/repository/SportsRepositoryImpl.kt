package com.sangtq.weatherappkmp.data.repository

import com.sangtq.weatherappkmp.data.mapper.toDomain
import com.sangtq.weatherappkmp.domain.model.SportsData
import com.sangtq.weatherappkmp.domain.repository.SportsRepository
import com.sangtq.weatherappkmp.network.WeatherApiClient

class SportsRepositoryImpl(private val apiClient: WeatherApiClient) : SportsRepository {

    private val cache = mutableMapOf<String, SportsData>()

    override suspend fun getSports(query: String): Result<SportsData> {
        cache[query]?.let { return Result.success(it) }
        return apiClient.getSports(query)
            .map { it.toDomain() }
            .also { result -> result.onSuccess { cache[query] = it } }
    }
}
