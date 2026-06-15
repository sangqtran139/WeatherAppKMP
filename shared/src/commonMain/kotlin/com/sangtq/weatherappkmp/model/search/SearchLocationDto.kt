package com.sangtq.weatherappkmp.model.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchLocationDto(
    val id: Int = 0,
    val name: String = "",
    val region: String = "",
    val country: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val url: String = ""
)
