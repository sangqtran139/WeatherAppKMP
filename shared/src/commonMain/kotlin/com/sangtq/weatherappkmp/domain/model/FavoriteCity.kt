package com.sangtq.weatherappkmp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteCity(
    val name: String,
    val country: String = "",
    val query: String
)
