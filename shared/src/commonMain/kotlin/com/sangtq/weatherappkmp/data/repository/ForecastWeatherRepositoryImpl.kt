package com.sangtq.weatherappkmp.data.repository

import com.sangtq.weatherappkmp.data.mapper.toDomain
import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.domain.repository.ForecastWeatherRepository
import com.sangtq.weatherappkmp.network.WeatherApiClient

class ForecastWeatherRepositoryImpl(
    private val apiClient: WeatherApiClient
) : ForecastWeatherRepository {

    private var cacheKey: String? = null
    private var cache: WeatherData? = null

    override suspend fun getForecastWeather(location: String, days: Int): Result<WeatherData> {
        if (cacheKey == location && cache != null) return Result.success(cache!!)
        return apiClient.getForecastWeather(location, days)
            .mapCatching { dto ->
                dto.error?.takeIf { it.message.isNotBlank() }?.let { error(it.message) }
                dto.message?.let { error(it) }
                dto.toDomain()
            }
            .also { result -> result.onSuccess { cache = it; cacheKey = location } }
    }

    override fun clearCache() {
        cache = null
        cacheKey = null
    }
}
