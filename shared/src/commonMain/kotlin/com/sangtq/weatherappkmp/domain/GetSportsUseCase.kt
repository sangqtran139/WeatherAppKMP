package com.sangtq.weatherappkmp.domain

import com.sangtq.weatherappkmp.domain.model.SportsData
import com.sangtq.weatherappkmp.domain.repository.SportsRepository

class GetSportsUseCase(private val repository: SportsRepository) {
    suspend operator fun invoke(query: String = "global"): Result<SportsData> {
        return repository.getSports(query)
    }
}
