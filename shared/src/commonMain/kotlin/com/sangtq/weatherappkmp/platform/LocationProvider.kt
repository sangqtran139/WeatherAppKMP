package com.sangtq.weatherappkmp.platform

import com.sangtq.weatherappkmp.domain.model.LocationResult

expect class LocationProvider {
    suspend fun getCurrentLocation(): LocationResult
}
