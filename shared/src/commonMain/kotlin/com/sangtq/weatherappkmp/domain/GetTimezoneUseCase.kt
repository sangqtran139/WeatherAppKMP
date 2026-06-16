package com.sangtq.weatherappkmp.domain

import com.sangtq.weatherappkmp.domain.model.TimezoneInfo
import com.sangtq.weatherappkmp.domain.repository.TimezoneRepository

class GetTimezoneUseCase(private val repository: TimezoneRepository) {
    suspend operator fun invoke(location: String): Result<TimezoneInfo> {
        if (location.isBlank()) return Result.failure(IllegalArgumentException("Location cannot be empty"))
        return repository.getTimezone(location.trim())
    }
}
