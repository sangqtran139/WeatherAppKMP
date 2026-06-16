package com.sangtq.weatherappkmp.domain.model

data class TimezoneInfo(
    val name: String,
    val country: String,
    val tzId: String,
    val localtimeEpoch: Long,
    val localtime: String
)
