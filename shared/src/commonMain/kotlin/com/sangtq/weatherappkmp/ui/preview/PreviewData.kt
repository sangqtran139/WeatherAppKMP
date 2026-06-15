package com.sangtq.weatherappkmp.ui.preview

import com.sangtq.weatherappkmp.domain.model.AstroData
import com.sangtq.weatherappkmp.domain.model.CurrentWeather
import com.sangtq.weatherappkmp.domain.model.DaySummary
import com.sangtq.weatherappkmp.domain.model.ForecastDay
import com.sangtq.weatherappkmp.domain.model.HourWeather
import com.sangtq.weatherappkmp.domain.model.SearchLocation
import com.sangtq.weatherappkmp.domain.model.WeatherCondition
import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.domain.model.WeatherLocation

internal val previewConditionSunny = WeatherCondition(code = 1000, text = "Sunny", icon = "")
internal val previewConditionRainy = WeatherCondition(code = 1063, text = "Patchy rain nearby", icon = "")

internal val previewHourWeather = HourWeather(
    timeEpoch = 1781510400,
    time = "2026-06-15 14:00",
    tempC = 35.0,
    isDay = true,
    condition = previewConditionSunny,
    windKph = 10.1,
    windDir = "SE",
    pressureMb = 1001.0,
    precipMm = 0.0,
    humidity = 65,
    chanceOfRain = 5,
    chanceOfSnow = 0,
    uv = 5.0
)

internal val previewDaySummary = DaySummary(
    maxTempC = 37.2,
    minTempC = 28.4,
    avgTempC = 31.1,
    maxWindKph = 17.6,
    totalPrecipMm = 2.1,
    avgHumidity = 67,
    dailyChanceOfRain = 57,
    dailyChanceOfSnow = 0,
    condition = previewConditionRainy,
    uv = 11.6
)

internal val previewAstro = AstroData(
    sunrise = "05:15 AM",
    sunset = "06:39 PM",
    moonrise = "04:39 AM",
    moonset = "06:49 PM"
)

internal val previewForecastDay = ForecastDay(
    date = "2026-06-15",
    dateEpoch = 1781481600,
    day = previewDaySummary,
    astro = previewAstro,
    hours = List(24) { index -> previewHourWeather.copy(timeEpoch = 1781456400 + index * 3600L) }
)

internal val previewForecastDayList = listOf(
    previewForecastDay,
    previewForecastDay.copy(date = "2026-06-16", dateEpoch = 1781568000),
    previewForecastDay.copy(date = "2026-06-17", dateEpoch = 1781654400)
)

internal val previewWeatherLocation = WeatherLocation(
    name = "Hanoi",
    region = "",
    country = "Vietnam",
    lat = 21.03,
    lon = 105.85,
    timezoneId = "Asia/Bangkok",
    localtimeEpoch = 1781538124,
    localtime = "2026-06-15 22:42"
)

internal val previewCurrentWeather = CurrentWeather(
    tempC = 27.4,
    feelsLikeC = 33.3,
    isDay = false,
    condition = WeatherCondition(code = 1183, text = "Light rain", icon = ""),
    windKph = 11.5,
    windDir = "E",
    pressureMb = 1006.0,
    precipMm = 0.0,
    humidity = 94,
    uv = 0.0,
    visKm = 10.0,
    gustKph = 17.0
)

internal val previewWeatherData = WeatherData(
    location = previewWeatherLocation,
    current = previewCurrentWeather,
    forecastDays = previewForecastDayList
)

internal val previewSearchLocations = listOf(
    SearchLocation(id = 1, name = "Ha Noi", region = "", country = "Vietnam", lat = 21.03, lon = 105.85, url = "ha-noi-vietnam"),
    SearchLocation(id = 2, name = "Ho Chi Minh City", region = "", country = "Vietnam", lat = 10.82, lon = 106.63, url = "ho-chi-minh-city-vietnam")
)
