package com.sangtq.weatherappkmp.domain.repository

import com.sangtq.weatherappkmp.domain.model.IpLocation

interface IpLocationRepository {
    suspend fun getLocationByIp(): Result<IpLocation>
}
