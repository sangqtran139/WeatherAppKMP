package com.sangtq.weatherappkmp.domain.repository

import com.sangtq.weatherappkmp.domain.model.MarineForecast

interface MarineRepository {
    suspend fun getMarine(location: String, days: Int = 3): Result<MarineForecast>
}
