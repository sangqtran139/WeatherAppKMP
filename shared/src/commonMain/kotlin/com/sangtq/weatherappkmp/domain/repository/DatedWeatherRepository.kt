package com.sangtq.weatherappkmp.domain.repository

import com.sangtq.weatherappkmp.domain.model.WeatherData

interface HistoryWeatherRepository {
    suspend fun getHistory(location: String, date: String, language: String = ""): Result<WeatherData>
}

interface FutureWeatherRepository {
    suspend fun getFuture(location: String, date: String, language: String = ""): Result<WeatherData>
}
