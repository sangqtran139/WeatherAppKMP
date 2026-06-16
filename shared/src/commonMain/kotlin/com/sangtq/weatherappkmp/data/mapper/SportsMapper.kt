package com.sangtq.weatherappkmp.data.mapper

import com.sangtq.weatherappkmp.domain.model.SportEvent
import com.sangtq.weatherappkmp.domain.model.SportsData
import com.sangtq.weatherappkmp.model.sports.SportEventDto
import com.sangtq.weatherappkmp.model.sports.SportsResponseDto

fun SportsResponseDto.toDomain() = SportsData(
    football = football.map { it.toDomain() },
    cricket = cricket.map { it.toDomain() },
    golf = golf.map { it.toDomain() }
)

fun SportEventDto.toDomain() = SportEvent(
    stadium = stadium,
    country = country,
    region = region,
    tournament = tournament,
    start = start,
    match = match
)
