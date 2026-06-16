package com.sangtq.weatherappkmp.model.astronomy

import com.sangtq.weatherappkmp.model.forecast.Location
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AstronomyResponseDto(
    val location: Location? = null,
    val astronomy: AstronomyContainerDto? = null
)

@Serializable
data class AstronomyContainerDto(
    val astro: AstroDetailsDto? = null
)

@Serializable
data class AstroDetailsDto(
    val sunrise: String = "",
    val sunset: String = "",
    val moonrise: String = "",
    val moonset: String = "",
    @SerialName("moon_phase") val moonPhase: String = "",
    @SerialName("moon_illumination") val moonIllumination: Int = 0,
    @SerialName("is_moon_up") val isMoonUp: Int = 0,
    @SerialName("is_sun_up") val isSunUp: Int = 0
)
