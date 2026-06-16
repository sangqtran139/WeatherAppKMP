package com.sangtq.weatherappkmp.model.timezone

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimezoneResponseDto(
    val location: TimezoneLocationDto? = null
)

@Serializable
data class TimezoneLocationDto(
    val name: String = "",
    val region: String = "",
    val country: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    @SerialName("tz_id") val tzId: String = "",
    @SerialName("localtime_epoch") val localtimeEpoch: Long = 0,
    val localtime: String = ""
)
