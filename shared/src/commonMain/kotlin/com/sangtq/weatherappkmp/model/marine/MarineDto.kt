package com.sangtq.weatherappkmp.model.marine

import com.sangtq.weatherappkmp.model.forecast.Location
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarineResponseDto(
    val message: String? = null,
    val location: Location? = null,
    val forecast: MarineForecastContainer? = null
)

@Serializable
data class MarineForecastContainer(
    val forecastday: List<MarineForecastDayDto> = emptyList()
)

@Serializable
data class MarineForecastDayDto(
    val date: String = "",
    @SerialName("date_epoch") val dateEpoch: Long = 0,
    val day: MarineDayDto? = null,
    val hour: List<MarineHourDto> = emptyList()
)

@Serializable
data class MarineDayDto(
    @SerialName("maxtemp_c") val maxTempC: Double = 0.0,
    @SerialName("mintemp_c") val minTempC: Double = 0.0,
    @SerialName("avgtemp_c") val avgTempC: Double = 0.0,
    @SerialName("maxwind_kph") val maxWindKph: Double = 0.0,
    val tides: List<MarineTidesContainer> = emptyList()
)

@Serializable
data class MarineTidesContainer(
    val tide: List<MarineTideEntryDto> = emptyList()
)

@Serializable
data class MarineTideEntryDto(
    @SerialName("tide_time") val time: String = "",
    @SerialName("tide_height_mt") val heightMt: String = "",
    @SerialName("tide_type") val type: String = ""
)

@Serializable
data class MarineHourDto(
    val time: String = "",
    @SerialName("time_epoch") val timeEpoch: Long = 0,
    @SerialName("temp_c") val tempC: Double = 0.0,
    @SerialName("wind_kph") val windKph: Double = 0.0,
    @SerialName("wind_dir") val windDir: String = "",
    @SerialName("sig_ht_mt") val sigHtMt: Double = 0.0,
    @SerialName("swell_ht_mt") val swellHtMt: Double = 0.0,
    @SerialName("swell_dir_16_point") val swellDir: String = "",
    @SerialName("swell_period_secs") val swellPeriodSecs: Double = 0.0,
    @SerialName("water_temp_c") val waterTempC: Double = 0.0
)
