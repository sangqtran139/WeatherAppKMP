package com.sangtq.weatherappkmp.data.mapper

import com.sangtq.weatherappkmp.domain.model.AstroData
import com.sangtq.weatherappkmp.domain.model.CurrentWeather
import com.sangtq.weatherappkmp.domain.model.DaySummary
import com.sangtq.weatherappkmp.domain.model.ForecastDay
import com.sangtq.weatherappkmp.domain.model.HourWeather
import com.sangtq.weatherappkmp.domain.model.WeatherCondition
import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.domain.model.WeatherLocation
import com.sangtq.weatherappkmp.model.forecast.Astro
import com.sangtq.weatherappkmp.model.forecast.Condition
import com.sangtq.weatherappkmp.model.forecast.Current
import com.sangtq.weatherappkmp.model.forecast.Day
import com.sangtq.weatherappkmp.model.forecast.Forecastday
import com.sangtq.weatherappkmp.model.forecast.ForecastWeatherDto
import com.sangtq.weatherappkmp.model.forecast.Hour
import com.sangtq.weatherappkmp.model.forecast.Location

private val emptyCondition = WeatherCondition("", "", 0)

fun ForecastWeatherDto.toDomain() = WeatherData(
    location = location?.toDomain() ?: WeatherLocation("", "", "", "", 0L, 0.0, 0.0, ""),
    current = current?.toDomain() ?: CurrentWeather(0.0, 0.0, emptyCondition, 0, 0.0, "", 0.0, 0.0, 0.0, 0.0, false, 0.0),
    forecastDays = forecast?.forecastday?.map { it.toDomain() } ?: emptyList()
)

fun Location.toDomain() = WeatherLocation(
    name = name,
    country = country,
    region = region,
    localtime = localtime,
    localtimeEpoch = localtimeEpoch,
    lat = lat,
    lon = lon,
    timezoneId = tzId
)

fun Current.toDomain() = CurrentWeather(
    tempC = tempC,
    feelsLikeC = feelslikeC,
    condition = condition?.toDomain() ?: emptyCondition,
    humidity = humidity,
    windKph = windKph,
    windDir = windDir,
    uv = uv,
    precipMm = precipMm,
    pressureMb = pressureMb,
    visKm = visKm,
    isDay = isDay == 1,
    gustKph = gustKph
)

fun Condition.toDomain() = WeatherCondition(text = text, icon = icon, code = code)

fun Forecastday.toDomain() = ForecastDay(
    date = date,
    dateEpoch = dateEpoch,
    day = day?.toDomain() ?: DaySummary(0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0, emptyCondition, 0.0),
    astro = astro?.toDomain() ?: AstroData("", "", "", ""),
    hours = hour?.map { it.toDomain() } ?: emptyList()
)

fun Day.toDomain() = DaySummary(
    maxTempC = maxtempC,
    minTempC = mintempC,
    avgTempC = avgtempC,
    maxWindKph = maxwindKph,
    totalPrecipMm = totalprecipMm,
    avgHumidity = avghumidity,
    dailyChanceOfRain = dailyChanceOfRain,
    dailyChanceOfSnow = dailyChanceOfSnow,
    condition = condition?.toDomain() ?: emptyCondition,
    uv = uv
)

fun Hour.toDomain() = HourWeather(
    timeEpoch = timeEpoch,
    time = time,
    tempC = tempC,
    condition = condition?.toDomain() ?: emptyCondition,
    windKph = windKph,
    windDir = windDir,
    humidity = humidity,
    chanceOfRain = chanceOfRain,
    chanceOfSnow = chanceOfSnow,
    uv = uv,
    isDay = isDay == 1,
    precipMm = precipMm,
    pressureMb = pressureMb
)

fun Astro.toDomain() = AstroData(
    sunrise = sunrise,
    sunset = sunset,
    moonrise = moonrise,
    moonset = moonset
)
