package com.sangtq.weatherappkmp.domain.repository

import com.sangtq.weatherappkmp.domain.model.WeatherData

interface ForecastWeatherRepository {
    suspend fun getForecastWeather(location: String, days: Int): Result<WeatherData>
    fun clearCache()
}
