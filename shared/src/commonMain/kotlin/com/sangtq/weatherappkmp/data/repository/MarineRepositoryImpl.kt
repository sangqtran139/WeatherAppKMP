package com.sangtq.weatherappkmp.data.repository

import com.sangtq.weatherappkmp.data.mapper.toDomain
import com.sangtq.weatherappkmp.domain.model.MarineForecast
import com.sangtq.weatherappkmp.domain.repository.MarineRepository
import com.sangtq.weatherappkmp.network.WeatherApiClient

class MarineRepositoryImpl(private val apiClient: WeatherApiClient) : MarineRepository {

    private val cache = mutableMapOf<String, MarineForecast>()

    override suspend fun getMarine(location: String, days: Int, language: String): Result<MarineForecast> {
        val key = "$location#$days#$language"
        cache[key]?.let { return Result.success(it) }
        return apiClient.getMarine(location, days, language)
            .mapCatching { dto ->
                dto.message?.let { error(it) }
                dto.toDomain()
            }
            .also { result -> result.onSuccess { cache[key] = it } }
    }
}
