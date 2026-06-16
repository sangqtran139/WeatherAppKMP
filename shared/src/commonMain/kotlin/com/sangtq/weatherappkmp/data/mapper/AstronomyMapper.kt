package com.sangtq.weatherappkmp.data.mapper

import com.sangtq.weatherappkmp.domain.model.AstronomyDetail
import com.sangtq.weatherappkmp.model.astronomy.AstronomyResponseDto

fun AstronomyResponseDto.toDomain(date: String): AstronomyDetail {
    val astro = astronomy?.astro
    return AstronomyDetail(
        locationName = location?.name.orEmpty(),
        country = location?.country.orEmpty(),
        date = date,
        sunrise = astro?.sunrise.orEmpty(),
        sunset = astro?.sunset.orEmpty(),
        moonrise = astro?.moonrise.orEmpty(),
        moonset = astro?.moonset.orEmpty(),
        moonPhase = astro?.moonPhase.orEmpty(),
        moonIllumination = astro?.moonIllumination ?: 0,
        isMoonUp = (astro?.isMoonUp ?: 0) == 1,
        isSunUp = (astro?.isSunUp ?: 0) == 1
    )
}
