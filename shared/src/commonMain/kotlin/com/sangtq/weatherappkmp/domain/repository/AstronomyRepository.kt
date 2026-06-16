package com.sangtq.weatherappkmp.domain.repository

import com.sangtq.weatherappkmp.domain.model.AstronomyDetail

interface AstronomyRepository {
    suspend fun getAstronomy(location: String, date: String): Result<AstronomyDetail>
}
