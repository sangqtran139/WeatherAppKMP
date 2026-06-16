package com.sangtq.weatherappkmp.domain

import com.sangtq.weatherappkmp.domain.model.AstronomyDetail
import com.sangtq.weatherappkmp.domain.repository.AstronomyRepository

class GetAstronomyUseCase(private val repository: AstronomyRepository) {
    suspend operator fun invoke(location: String, date: String): Result<AstronomyDetail> {
        if (location.isBlank()) return Result.failure(IllegalArgumentException("Location cannot be empty"))
        if (date.isBlank()) return Result.failure(IllegalArgumentException("Date cannot be empty"))
        return repository.getAstronomy(location.trim(), date)
    }
}
