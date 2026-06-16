package com.sangtq.weatherappkmp.domain

import com.sangtq.weatherappkmp.domain.model.IpLocation
import com.sangtq.weatherappkmp.domain.repository.IpLocationRepository

class GetLocationByIpUseCase(private val repository: IpLocationRepository) {
    suspend operator fun invoke(): Result<IpLocation> = repository.getLocationByIp()
}
