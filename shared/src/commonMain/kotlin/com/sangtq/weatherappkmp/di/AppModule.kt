package com.sangtq.weatherappkmp.di

import com.sangtq.weatherappkmp.data.repository.AstronomyRepositoryImpl
import com.sangtq.weatherappkmp.data.storage.PreferencesStorage
import com.sangtq.weatherappkmp.data.repository.ForecastWeatherRepositoryImpl
import com.sangtq.weatherappkmp.data.repository.FutureWeatherRepositoryImpl
import com.sangtq.weatherappkmp.data.repository.HistoryWeatherRepositoryImpl
import com.sangtq.weatherappkmp.data.repository.IpLocationRepositoryImpl
import com.sangtq.weatherappkmp.data.repository.MarineRepositoryImpl
import com.sangtq.weatherappkmp.data.repository.SearchRepositoryImpl
import com.sangtq.weatherappkmp.data.repository.SportsRepositoryImpl
import com.sangtq.weatherappkmp.data.repository.TimezoneRepositoryImpl
import com.sangtq.weatherappkmp.domain.ForecastWeatherUseCase
import com.sangtq.weatherappkmp.domain.GetAstronomyUseCase
import com.sangtq.weatherappkmp.domain.GetFutureWeatherUseCase
import com.sangtq.weatherappkmp.domain.GetHistoryWeatherUseCase
import com.sangtq.weatherappkmp.domain.GetLocationByIpUseCase
import com.sangtq.weatherappkmp.domain.GetMarineUseCase
import com.sangtq.weatherappkmp.domain.GetSportsUseCase
import com.sangtq.weatherappkmp.domain.GetTimezoneUseCase
import com.sangtq.weatherappkmp.domain.SearchLocationUseCase
import com.sangtq.weatherappkmp.domain.repository.AstronomyRepository
import com.sangtq.weatherappkmp.domain.repository.ForecastWeatherRepository
import com.sangtq.weatherappkmp.domain.repository.FutureWeatherRepository
import com.sangtq.weatherappkmp.domain.repository.HistoryWeatherRepository
import com.sangtq.weatherappkmp.domain.repository.IpLocationRepository
import com.sangtq.weatherappkmp.domain.repository.MarineRepository
import com.sangtq.weatherappkmp.domain.repository.SearchRepository
import com.sangtq.weatherappkmp.domain.repository.SportsRepository
import com.sangtq.weatherappkmp.domain.repository.TimezoneRepository
import com.sangtq.weatherappkmp.ui.astronomy.AstronomyViewModel
import com.sangtq.weatherappkmp.ui.future.FutureViewModel
import com.sangtq.weatherappkmp.ui.history.HistoryViewModel
import com.sangtq.weatherappkmp.ui.marine.MarineViewModel
import com.sangtq.weatherappkmp.ui.sports.SportsViewModel
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
    single<TimezoneRepository> { TimezoneRepositoryImpl(get()) }
    single<IpLocationRepository> { IpLocationRepositoryImpl(get()) }
    single<AstronomyRepository> { AstronomyRepositoryImpl(get()) }
    single<SportsRepository> { SportsRepositoryImpl(get()) }
    single<MarineRepository> { MarineRepositoryImpl(get()) }
    single<HistoryWeatherRepository> { HistoryWeatherRepositoryImpl(get()) }
    single<FutureWeatherRepository> { FutureWeatherRepositoryImpl(get()) }
    single { PreferencesStorage(get()) }
    single { ForecastWeatherUseCase(get()) }
    single { SearchLocationUseCase(get()) }
    single { GetTimezoneUseCase(get()) }
    single { GetLocationByIpUseCase(get()) }
    single { GetAstronomyUseCase(get()) }
    single { GetSportsUseCase(get()) }
    single { GetMarineUseCase(get()) }
    single { GetHistoryWeatherUseCase(get()) }
    single { GetFutureWeatherUseCase(get()) }
    viewModelOf(::WeatherHomeViewModel)
    viewModelOf(::WeatherDetailViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::AstronomyViewModel)
    viewModelOf(::SportsViewModel)
    viewModelOf(::MarineViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::FutureViewModel)
}
