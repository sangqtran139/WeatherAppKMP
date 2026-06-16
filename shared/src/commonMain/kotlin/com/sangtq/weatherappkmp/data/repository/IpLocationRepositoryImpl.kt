package com.sangtq.weatherappkmp.data.repository

import com.sangtq.weatherappkmp.data.mapper.toDomain
import com.sangtq.weatherappkmp.domain.model.IpLocation
import com.sangtq.weatherappkmp.domain.repository.IpLocationRepository
import com.sangtq.weatherappkmp.network.WeatherApiClient

class IpLocationRepositoryImpl(private val apiClient: WeatherApiClient) : IpLocationRepository {

    private var cache: IpLocation? = null

    override suspend fun getLocationByIp(): Result<IpLocation> {
        cache?.let { return Result.success(it) }
        return apiClient.getLocationByIp().map { it.toDomain() }
            .also { result -> result.onSuccess { cache = it } }
    }
}
