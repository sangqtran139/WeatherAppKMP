package com.sangtq.weatherappkmp.data.mapper

import com.sangtq.weatherappkmp.domain.model.MarineDay
import com.sangtq.weatherappkmp.domain.model.MarineForecast
import com.sangtq.weatherappkmp.domain.model.MarineHour
import com.sangtq.weatherappkmp.domain.model.MarineTide
import com.sangtq.weatherappkmp.model.marine.MarineForecastDayDto
import com.sangtq.weatherappkmp.model.marine.MarineHourDto
import com.sangtq.weatherappkmp.model.marine.MarineResponseDto
import com.sangtq.weatherappkmp.model.marine.MarineTideEntryDto

fun MarineResponseDto.toDomain() = MarineForecast(
    locationName = location?.name.orEmpty(),
    country = location?.country.orEmpty(),
    days = forecast?.forecastday?.map { it.toDomain() } ?: emptyList()
)

fun MarineForecastDayDto.toDomain() = MarineDay(
    date = date,
    dateEpoch = dateEpoch,
    maxTempC = day?.maxTempC ?: 0.0,
    minTempC = day?.minTempC ?: 0.0,
    maxWindKph = day?.maxWindKph ?: 0.0,
    tides = day?.tides?.flatMap { it.tide }?.map { it.toDomain() } ?: emptyList(),
    hours = hour.map { it.toDomain() }
)

fun MarineTideEntryDto.toDomain() = MarineTide(
    time = time,
    heightMeters = heightMt.toDoubleOrNull() ?: 0.0,
    type = type
)

fun MarineHourDto.toDomain() = MarineHour(
    time = time,
    timeEpoch = timeEpoch,
    tempC = tempC,
    windKph = windKph,
    windDir = windDir,
    sigWaveHtMeters = sigHtMt,
    swellHtMeters = swellHtMt,
    swellDir = swellDir,
    swellPeriodSecs = swellPeriodSecs,
    waterTempC = waterTempC
)
