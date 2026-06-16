package com.sangtq.weatherappkmp.domain.model

data class IpLocation(
    val ip: String,
    val city: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val tzId: String
) {
    val coordinatesQuery: String get() = "$lat,$lon"
}
