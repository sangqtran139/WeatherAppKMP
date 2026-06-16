package com.sangtq.weatherappkmp.domain.repository

import com.sangtq.weatherappkmp.domain.model.SportsData

interface SportsRepository {
    suspend fun getSports(query: String = "global"): Result<SportsData>
}
