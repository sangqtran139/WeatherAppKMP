package com.sangtq.weatherappkmp.domain.model

data class WeatherData(
    val location: WeatherLocation,
    val current: CurrentWeather,
    val forecastDays: List<ForecastDay>,
    val alerts: List<WeatherAlert> = emptyList()
)

data class WeatherAlert(
    val headline: String,
    val event: String,
    val severity: String,
    val areas: String,
    val effective: String,
    val expires: String,
    val description: String,
    val instruction: String
)

data class WeatherLocation(
    val name: String,
    val country: String,
    val region: String,
    val localtime: String,
    val localtimeEpoch: Long,
    val lat: Double,
    val lon: Double,
    val timezoneId: String
)

data class CurrentWeather(
    val tempC: Double,
    val feelsLikeC: Double,
    val condition: WeatherCondition,
    val humidity: Int,
    val windKph: Double,
    val windDir: String,
    val uv: Double,
    val precipMm: Double,
    val pressureMb: Double,
    val visKm: Double,
    val isDay: Boolean,
    val gustKph: Double
)

data class WeatherCondition(
    val text: String,
    val icon: String,
    val code: Int
)

data class ForecastDay(
    val date: String,
    val dateEpoch: Long,
    val day: DaySummary,
    val astro: AstroData,
    val hours: List<HourWeather>
)

data class DaySummary(
    val maxTempC: Double,
    val minTempC: Double,
    val avgTempC: Double,
    val maxWindKph: Double,
    val totalPrecipMm: Double,
    val avgHumidity: Int,
    val dailyChanceOfRain: Int,
    val dailyChanceOfSnow: Int,
    val condition: WeatherCondition,
    val uv: Double
)

data class HourWeather(
    val timeEpoch: Long,
    val time: String,
    val tempC: Double,
    val condition: WeatherCondition,
    val windKph: Double,
    val windDir: String,
    val humidity: Int,
    val chanceOfRain: Int,
    val chanceOfSnow: Int,
    val uv: Double,
    val isDay: Boolean,
    val precipMm: Double,
    val pressureMb: Double
)

data class AstroData(
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String
)
