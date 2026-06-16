package com.sangtq.weatherappkmp.data.mapper

import com.sangtq.weatherappkmp.domain.model.TimezoneInfo
import com.sangtq.weatherappkmp.model.timezone.TimezoneResponseDto

fun TimezoneResponseDto.toDomain(): TimezoneInfo? {
    val loc = location ?: return null
    return TimezoneInfo(
        name = loc.name,
        country = loc.country,
        tzId = loc.tzId,
        localtimeEpoch = loc.localtimeEpoch,
        localtime = loc.localtime
    )
}
