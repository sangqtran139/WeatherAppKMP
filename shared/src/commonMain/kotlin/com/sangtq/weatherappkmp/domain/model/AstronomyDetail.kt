package com.sangtq.weatherappkmp.domain.model

data class AstronomyDetail(
    val locationName: String,
    val country: String,
    val date: String,
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    val moonPhase: String,
    val moonIllumination: Int,
    val isMoonUp: Boolean,
    val isSunUp: Boolean
)
