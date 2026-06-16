package com.sangtq.weatherappkmp.di

import android.app.Application
import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import com.sangtq.weatherappkmp.platform.LocationProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin(application: Application, apiKey: String, isDebug: Boolean = false) {
    startKoin {
        androidContext(application)
        modules(
            appModule(apiKey, isDebug),
            module {
                single { LocationProvider(androidContext()) }
                single<Settings> {
                    val prefs = androidContext().getSharedPreferences("weather_app_prefs", Context.MODE_PRIVATE)
                    SharedPreferencesSettings(prefs)
                }
            }
        )
    }
}
