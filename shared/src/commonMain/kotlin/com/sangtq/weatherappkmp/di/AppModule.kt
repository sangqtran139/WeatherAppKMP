package com.sangtq.weatherappkmp.di

import com.sangtq.weatherappkmp.data.repository.ForecastWeatherRepositoryImpl
import com.sangtq.weatherappkmp.data.repository.SearchRepositoryImpl
import com.sangtq.weatherappkmp.domain.ForecastWeatherUseCase
import com.sangtq.weatherappkmp.domain.SearchLocationUseCase
import com.sangtq.weatherappkmp.domain.repository.ForecastWeatherRepository
import com.sangtq.weatherappkmp.domain.repository.SearchRepository
import com.sangtq.weatherappkmp.network.WeatherApiClient
import com.sangtq.weatherappkmp.network.createHttpClient
import com.sangtq.weatherappkmp.ui.detail.WeatherDetailViewModel
import com.sangtq.weatherappkmp.ui.home.WeatherHomeViewModel
import com.sangtq.weatherappkmp.ui.search.SearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun appModule(apiKey: String, isDebug: Boolean = false) = module {
    single { createHttpClient(isDebug) }
    single { WeatherApiClient(get(), apiKey) }
    single<ForecastWeatherRepository> { ForecastWeatherRepositoryImpl(get()) }
    single<SearchRepository> { SearchRepositoryImpl(get()) }
    single { ForecastWeatherUseCase(get()) }
    single { SearchLocationUseCase(get()) }
    viewModelOf(::WeatherHomeViewModel)
    viewModelOf(::WeatherDetailViewModel)
    viewModelOf(::SearchViewModel)
}
