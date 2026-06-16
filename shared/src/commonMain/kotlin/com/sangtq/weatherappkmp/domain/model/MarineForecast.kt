package com.sangtq.weatherappkmp.domain.model

data class MarineForecast(
    val locationName: String,
    val country: String,
    val timezoneId: String,
    val days: List<MarineDay>
)

data class MarineDay(
    val date: String,
    val dateEpoch: Long,
    val maxTempC: Double,
    val minTempC: Double,
    val maxWindKph: Double,
    val tides: List<MarineTide>,
    val hours: List<MarineHour>
)

data class MarineTide(
    val time: String,
    val heightMeters: Double,
    val type: String
)

data class MarineHour(
    val time: String,
    val timeEpoch: Long,
    val tempC: Double,
    val windKph: Double,
    val windDir: String,
    val sigWaveHtMeters: Double,
    val swellHtMeters: Double,
    val swellDir: String,
    val swellPeriodSecs: Double,
    val waterTempC: Double
)
