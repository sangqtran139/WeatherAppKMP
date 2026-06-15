package com.sangtq.weatherappkmp.domain.model

sealed class LocationResult {
    data class Success(val lat: Double, val lon: Double) : LocationResult()
    data object PermissionDenied : LocationResult()
    data class Error(val message: String) : LocationResult()
}
