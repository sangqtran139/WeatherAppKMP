package com.sangtq.weatherappkmp.domain

import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.domain.repository.ForecastWeatherRepository

class ForecastWeatherUseCase(private val repository: ForecastWeatherRepository) {
    suspend operator fun invoke(location: String, days: Int = 3, language: String = ""): Result<WeatherData> {
        if (location.isBlank()) return Result.failure(IllegalArgumentException("Location cannot be empty"))
        return repository.getForecastWeather(location.trim(), days, language)
    }
}
