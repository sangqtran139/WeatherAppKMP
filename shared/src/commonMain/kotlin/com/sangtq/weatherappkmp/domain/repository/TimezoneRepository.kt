package com.sangtq.weatherappkmp.domain.repository

import com.sangtq.weatherappkmp.domain.model.TimezoneInfo

interface TimezoneRepository {
    suspend fun getTimezone(location: String): Result<TimezoneInfo>
}
