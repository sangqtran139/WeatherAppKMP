package com.sangtq.weatherappkmp.data.repository

import com.sangtq.weatherappkmp.data.mapper.toDomain
import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.domain.repository.FutureWeatherRepository
import com.sangtq.weatherappkmp.domain.repository.HistoryWeatherRepository
import com.sangtq.weatherappkmp.network.WeatherApiClient

class HistoryWeatherRepositoryImpl(private val apiClient: WeatherApiClient) : HistoryWeatherRepository {
    private val cache = mutableMapOf<String, WeatherData>()
    override suspend fun getHistory(location: String, date: String): Result<WeatherData> {
        val key = "$location#$date"
        cache[key]?.let { return Result.success(it) }
        return apiClient.getHistory(location, date)
            .mapCatching { dto ->
                dto.error?.takeIf { it.message.isNotBlank() }?.let { error(it.message) }
                dto.message?.let { error(it) }
                dto.toDomain()
            }
            .also { result -> result.onSuccess { cache[key] = it } }
    }
}

class FutureWeatherRepositoryImpl(private val apiClient: WeatherApiClient) : FutureWeatherRepository {
    private val cache = mutableMapOf<String, WeatherData>()
    override suspend fun getFuture(location: String, date: String): Result<WeatherData> {
        val key = "$location#$date"
        cache[key]?.let { return Result.success(it) }
        return apiClient.getFuture(location, date)
            .mapCatching { dto ->
                dto.error?.takeIf { it.message.isNotBlank() }?.let { error(it.message) }
                dto.message?.let { error(it) }
                dto.toDomain()
            }
            .also { result -> result.onSuccess { cache[key] = it } }
    }
}
