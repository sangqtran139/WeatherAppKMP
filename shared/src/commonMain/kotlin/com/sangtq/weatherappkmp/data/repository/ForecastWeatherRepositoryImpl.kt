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

    override suspend fun getForecastWeather(location: String, days: Int, language: String): Result<WeatherData> {
        val key = "$location|$days|$language"
        if (cacheKey == key && cache != null) return Result.success(cache!!)
        return apiClient.getForecastWeather(location, days, language)
            .mapCatching { dto ->
                dto.error?.takeIf { it.message.isNotBlank() }?.let { error(it.message) }
                dto.message?.let { error(it) }
                dto.toDomain()
            }
            .also { result -> result.onSuccess { cache = it; cacheKey = key } }
    }

    override fun clearCache() {
        cache = null
        cacheKey = null
    }
}
