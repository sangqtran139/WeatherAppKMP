package com.sangtq.weatherappkmp.network

import com.sangtq.weatherappkmp.model.forecast.ForecastWeatherDto
import com.sangtq.weatherappkmp.model.search.SearchLocationDto
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
        dateTime: String = ""
    ): Result<ForecastWeatherDto> = runCatching {
        client.get("${ApiConfig.BASE_URL}/forecast.json") {
            header("x-rapidapi-key", apiKey)
            header("x-rapidapi-host", ApiConfig.API_HOST)
            parameter("q", location)
            if (days != null) parameter("days", days)
            if (language.isNotEmpty()) parameter("lang", language)
            if (dateTime.isNotEmpty()) parameter("dt", dateTime)
        }.body<ForecastWeatherDto>()
    }
}
