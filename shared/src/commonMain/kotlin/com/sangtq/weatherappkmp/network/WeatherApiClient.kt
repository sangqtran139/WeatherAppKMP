package com.sangtq.weatherappkmp.network

import com.sangtq.weatherappkmp.model.astronomy.AstronomyResponseDto
import com.sangtq.weatherappkmp.model.forecast.ForecastWeatherDto
import com.sangtq.weatherappkmp.model.ip.IpLookupDto
import com.sangtq.weatherappkmp.model.marine.MarineResponseDto
import com.sangtq.weatherappkmp.model.search.SearchLocationDto
import com.sangtq.weatherappkmp.model.sports.SportsResponseDto
import com.sangtq.weatherappkmp.model.timezone.TimezoneResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiConfig {
    const val BASE_URL = "https://weatherapi-com.p.rapidapi.com"
    const val API_HOST = "weatherapi-com.p.rapidapi.com"
}

fun createHttpClient(isDebug: Boolean = false): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    if (isDebug) {
        install(Logging) {
            level = LogLevel.INFO
        }
    }
}

class WeatherApiClient(private val client: HttpClient, private val apiKey: String) {

    suspend fun getHistory(location: String, date: String, language: String = ""): Result<ForecastWeatherDto> = runCatching {
        client.get("${ApiConfig.BASE_URL}/history.json") {
            header("x-rapidapi-key", apiKey)
            header("x-rapidapi-host", ApiConfig.API_HOST)
            parameter("q", location)
            parameter("dt", date)
            if (language.isNotEmpty()) parameter("lang", language)
        }.body<ForecastWeatherDto>()
    }

    suspend fun getFuture(location: String, date: String, language: String = ""): Result<ForecastWeatherDto> = runCatching {
        client.get("${ApiConfig.BASE_URL}/future.json") {
            header("x-rapidapi-key", apiKey)
            header("x-rapidapi-host", ApiConfig.API_HOST)
            parameter("q", location)
            parameter("dt", date)
            if (language.isNotEmpty()) parameter("lang", language)
        }.body<ForecastWeatherDto>()
    }

    suspend fun getMarine(location: String, days: Int = 3, language: String = ""): Result<MarineResponseDto> = runCatching {
        client.get("${ApiConfig.BASE_URL}/marine.json") {
            header("x-rapidapi-key", apiKey)
            header("x-rapidapi-host", ApiConfig.API_HOST)
            parameter("q", location)
            parameter("days", days)
            if (language.isNotEmpty()) parameter("lang", language)
        }.body<MarineResponseDto>()
    }

    suspend fun getSports(query: String = "global"): Result<SportsResponseDto> = runCatching {
        client.get("${ApiConfig.BASE_URL}/sports.json") {
            header("x-rapidapi-key", apiKey)
            header("x-rapidapi-host", ApiConfig.API_HOST)
            parameter("q", query)
        }.body<SportsResponseDto>()
    }

    suspend fun getAstronomy(location: String, date: String): Result<AstronomyResponseDto> = runCatching {
        client.get("${ApiConfig.BASE_URL}/astronomy.json") {
            header("x-rapidapi-key", apiKey)
            header("x-rapidapi-host", ApiConfig.API_HOST)
            parameter("q", location)
            parameter("dt", date)
        }.body<AstronomyResponseDto>()
    }

    suspend fun getLocationByIp(ip: String = "auto:ip"): Result<IpLookupDto> = runCatching {
        client.get("${ApiConfig.BASE_URL}/ip.json") {
            header("x-rapidapi-key", apiKey)
            header("x-rapidapi-host", ApiConfig.API_HOST)
            parameter("q", ip)
        }.body<IpLookupDto>()
    }

    suspend fun getTimezone(location: String): Result<TimezoneResponseDto> = runCatching {
        client.get("${ApiConfig.BASE_URL}/timezone.json") {
            header("x-rapidapi-key", apiKey)
            header("x-rapidapi-host", ApiConfig.API_HOST)
            parameter("q", location)
        }.body<TimezoneResponseDto>()
    }

    suspend fun searchLocations(query: String): Result<List<SearchLocationDto>> = runCatching {
        client.get("${ApiConfig.BASE_URL}/search.json") {
            header("x-rapidapi-key", apiKey)
            header("x-rapidapi-host", ApiConfig.API_HOST)
            parameter("q", query)
        }.body<List<SearchLocationDto>>()
    }

    suspend fun getForecastWeather(
        location: String,
        days: Int? = null,
        language: String = "",
        dateTime: String = "",
        alerts: Boolean = true
    ): Result<ForecastWeatherDto> = runCatching {
        client.get("${ApiConfig.BASE_URL}/forecast.json") {
            header("x-rapidapi-key", apiKey)
            header("x-rapidapi-host", ApiConfig.API_HOST)
            parameter("q", location)
            if (days != null) parameter("days", days)
            if (language.isNotEmpty()) parameter("lang", language)
            if (dateTime.isNotEmpty()) parameter("dt", dateTime)
            if (alerts) parameter("alerts", "yes")
        }.body<ForecastWeatherDto>()
    }
}
