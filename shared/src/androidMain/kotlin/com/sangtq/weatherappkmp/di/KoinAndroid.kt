package com.sangtq.weatherappkmp.di

import android.app.Application
import com.sangtq.weatherappkmp.platform.LocationProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin(application: Application, apiKey: String, isDebug: Boolean = false) {
    startKoin {
        androidContext(application)
        modules(
            appModule(apiKey, isDebug),
            module { single { LocationProvider(androidContext()) } }
        )
    }
}
