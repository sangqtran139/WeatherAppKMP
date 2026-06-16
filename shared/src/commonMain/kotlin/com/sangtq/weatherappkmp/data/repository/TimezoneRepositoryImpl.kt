package com.sangtq.weatherappkmp.data.repository

import com.sangtq.weatherappkmp.data.mapper.toDomain
import com.sangtq.weatherappkmp.domain.model.TimezoneInfo
import com.sangtq.weatherappkmp.domain.repository.TimezoneRepository
import com.sangtq.weatherappkmp.network.WeatherApiClient

class TimezoneRepositoryImpl(private val apiClient: WeatherApiClient) : TimezoneRepository {

    private val cache = mutableMapOf<String, TimezoneInfo>()

    override suspend fun getTimezone(location: String): Result<TimezoneInfo> {
        cache[location]?.let { return Result.success(it) }
        return apiClient.getTimezone(location).mapCatching {
            it.toDomain() ?: error("Empty timezone response")
        }.also { result -> result.onSuccess { cache[location] = it } }
    }
}
