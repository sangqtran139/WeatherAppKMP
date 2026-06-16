package com.sangtq.weatherappkmp.data.repository

import com.sangtq.weatherappkmp.data.mapper.toDomain
import com.sangtq.weatherappkmp.domain.model.AstronomyDetail
import com.sangtq.weatherappkmp.domain.repository.AstronomyRepository
import com.sangtq.weatherappkmp.network.WeatherApiClient

class AstronomyRepositoryImpl(private val apiClient: WeatherApiClient) : AstronomyRepository {

    private val cache = mutableMapOf<String, AstronomyDetail>()

    override suspend fun getAstronomy(location: String, date: String): Result<AstronomyDetail> {
        val key = "$location#$date"
        cache[key]?.let { return Result.success(it) }
        return apiClient.getAstronomy(location, date)
            .map { it.toDomain(date) }
            .also { result -> result.onSuccess { cache[key] = it } }
    }
}
