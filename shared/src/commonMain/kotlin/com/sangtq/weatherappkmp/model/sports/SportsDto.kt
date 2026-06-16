package com.sangtq.weatherappkmp.model.sports

import kotlinx.serialization.Serializable

@Serializable
data class SportsResponseDto(
    val football: List<SportEventDto> = emptyList(),
    val cricket: List<SportEventDto> = emptyList(),
    val golf: List<SportEventDto> = emptyList()
)

@Serializable
data class SportEventDto(
    val stadium: String = "",
    val country: String = "",
    val region: String = "",
    val tournament: String = "",
    val start: String = "",
    val match: String = ""
)
