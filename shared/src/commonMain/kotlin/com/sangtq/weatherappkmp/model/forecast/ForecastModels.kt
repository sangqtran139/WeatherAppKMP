package com.sangtq.weatherappkmp.model.forecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastWeatherDto(
    val current: Current? = null,
    val forecast: ForecastX? = null,
    val location: Location? = null,
    val alerts: AlertsContainer? = null
)

@Serializable
data class AlertsContainer(
    val alert: List<AlertDto> = emptyList()
)

@Serializable
data class AlertDto(
    val headline: String = "",
    val msgtype: String = "",
    val severity: String = "",
    val urgency: String = "",
    val areas: String = "",
    val category: String = "",
    val certainty: String = "",
    val event: String = "",
    val note: String = "",
    val effective: String = "",
    val expires: String = "",
    val desc: String = "",
    val instruction: String = ""
)

@Serializable
data class Current(
    val cloud: Int = 0,
    val condition: Condition? = null,
    @SerialName("feelslike_c") val feelslikeC: Double = 0.0,
    @SerialName("feelslike_f") val feelslikeF: Double = 0.0,
    @SerialName("gust_kph") val gustKph: Double = 0.0,
    @SerialName("gust_mph") val gustMph: Double = 0.0,
    val humidity: Int = 0,
    @SerialName("is_day") val isDay: Int = 0,
    @SerialName("last_updated") val lastUpdated: String = "",
    @SerialName("last_updated_epoch") val lastUpdatedEpoch: Long = 0,
    @SerialName("precip_in") val precipIn: Double = 0.0,
    @SerialName("precip_mm") val precipMm: Double = 0.0,
    @SerialName("pressure_in") val pressureIn: Double = 0.0,
    @SerialName("pressure_mb") val pressureMb: Double = 0.0,
    @SerialName("temp_c") val tempC: Double = 0.0,
    @SerialName("temp_f") val tempF: Double = 0.0,
    val uv: Double = 0.0,
    @SerialName("vis_km") val visKm: Double = 0.0,
    @SerialName("vis_miles") val visMiles: Double = 0.0,
    @SerialName("wind_degree") val windDegree: Int = 0,
    @SerialName("wind_dir") val windDir: String = "",
    @SerialName("wind_kph") val windKph: Double = 0.0,
    @SerialName("wind_mph") val windMph: Double = 0.0
)

@Serializable
data class ForecastX(
    val forecastday: List<Forecastday> = emptyList()
)

@Serializable
data class Forecastday(
    val astro: Astro? = null,
    val date: String = "",
    @SerialName("date_epoch") val dateEpoch: Long = 0,
    val day: Day? = null,
    val hour: List<Hour>? = null
)

@Serializable
data class Day(
    val avghumidity: Int = 0,
    @SerialName("avgtemp_c") val avgtempC: Double = 0.0,
    @SerialName("avgtemp_f") val avgtempF: Double = 0.0,
    @SerialName("avgvis_km") val avgvisKm: Double = 0.0,
    @SerialName("avgvis_miles") val avgvisMiles: Double = 0.0,
    val condition: Condition? = null,
    @SerialName("daily_chance_of_rain") val dailyChanceOfRain: Int = 0,
    @SerialName("daily_chance_of_snow") val dailyChanceOfSnow: Int = 0,
    @SerialName("daily_will_it_rain") val dailyWillItRain: Int = 0,
    @SerialName("daily_will_it_snow") val dailyWillItSnow: Int = 0,
    @SerialName("maxtemp_c") val maxtempC: Double = 0.0,
    @SerialName("maxtemp_f") val maxtempF: Double = 0.0,
    @SerialName("maxwind_kph") val maxwindKph: Double = 0.0,
    @SerialName("maxwind_mph") val maxwindMph: Double = 0.0,
    @SerialName("mintemp_c") val mintempC: Double = 0.0,
    @SerialName("mintemp_f") val mintempF: Double = 0.0,
    @SerialName("totalprecip_in") val totalprecipIn: Double = 0.0,
    @SerialName("totalprecip_mm") val totalprecipMm: Double = 0.0,
    @SerialName("totalsnow_cm") val totalsnowCm: Double = 0.0,
    val uv: Double = 0.0
)

@Serializable
data class Hour(
    @SerialName("chance_of_rain") val chanceOfRain: Int = 0,
    @SerialName("chance_of_snow") val chanceOfSnow: Int = 0,
    val cloud: Int = 0,
    val condition: Condition? = null,
    val humidity: Int = 0,
    @SerialName("is_day") val isDay: Int = 0,
    @SerialName("precip_in") val precipIn: Double = 0.0,
    @SerialName("precip_mm") val precipMm: Double = 0.0,
    @SerialName("pressure_in") val pressureIn: Double = 0.0,
    @SerialName("pressure_mb") val pressureMb: Double = 0.0,
    @SerialName("temp_c") val tempC: Double = 0.0,
    @SerialName("temp_f") val tempF: Double = 0.0,
    val time: String = "",
    @SerialName("time_epoch") val timeEpoch: Long = 0,
    val uv: Double = 0.0,
    @SerialName("vis_km") val visKm: Double = 0.0,
    @SerialName("vis_miles") val visMiles: Double = 0.0,
    @SerialName("will_it_rain") val willItRain: Int = 0,
    @SerialName("will_it_snow") val willItSnow: Int = 0,
    @SerialName("wind_degree") val windDegree: Int = 0,
    @SerialName("wind_dir") val windDir: String = "",
    @SerialName("wind_kph") val windKph: Double = 0.0,
    @SerialName("wind_mph") val windMph: Double = 0.0
)

@Serializable
data class Condition(
    val code: Int = 0,
    val icon: String = "",
    val text: String = ""
)

@Serializable
data class Astro(
    val moonrise: String = "",
    val moonset: String = "",
    val sunrise: String = "",
    val sunset: String = ""
)

@Serializable
data class Location(
    val country: String = "",
    val lat: Double = 0.0,
    val localtime: String = "",
    @SerialName("localtime_epoch") val localtimeEpoch: Long = 0,
    val lon: Double = 0.0,
    val name: String = "",
    val region: String = "",
    @SerialName("tz_id") val tzId: String = ""
)
