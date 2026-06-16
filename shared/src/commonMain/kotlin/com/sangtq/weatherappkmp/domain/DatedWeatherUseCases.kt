package com.sangtq.weatherappkmp.domain

import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.domain.repository.FutureWeatherRepository
import com.sangtq.weatherappkmp.domain.repository.HistoryWeatherRepository

class GetHistoryWeatherUseCase(private val repository: HistoryWeatherRepository) {
    suspend operator fun invoke(location: String, date: String): Result<WeatherData> {
        if (location.isBlank()) return Result.failure(IllegalArgumentException("Location cannot be empty"))
        if (date.isBlank()) return Result.failure(IllegalArgumentException("Date cannot be empty"))
        return repository.getHistory(location.trim(), date)
    }
}

class GetFutureWeatherUseCase(private val repository: FutureWeatherRepository) {
    suspend operator fun invoke(location: String, date: String): Result<WeatherData> {
        if (location.isBlank()) return Result.failure(IllegalArgumentException("Location cannot be empty"))
        if (date.isBlank()) return Result.failure(IllegalArgumentException("Date cannot be empty"))
        return repository.getFuture(location.trim(), date)
    }
}
