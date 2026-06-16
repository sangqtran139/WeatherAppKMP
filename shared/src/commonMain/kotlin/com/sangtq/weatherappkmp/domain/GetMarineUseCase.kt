package com.sangtq.weatherappkmp.domain

import com.sangtq.weatherappkmp.domain.model.MarineForecast
import com.sangtq.weatherappkmp.domain.repository.MarineRepository

class GetMarineUseCase(private val repository: MarineRepository) {
    suspend operator fun invoke(location: String, days: Int = 3, language: String = ""): Result<MarineForecast> {
        if (location.isBlank()) return Result.failure(IllegalArgumentException("Location cannot be empty"))
        return repository.getMarine(location.trim(), days, language)
    }
}
