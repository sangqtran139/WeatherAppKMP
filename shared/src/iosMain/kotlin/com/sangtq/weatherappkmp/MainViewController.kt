package com.sangtq.weatherappkmp

import androidx.compose.ui.window.ComposeUIViewController
import com.sangtq.weatherappkmp.di.appModule
import com.sangtq.weatherappkmp.platform.LocationProvider
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun MainViewController(apiKey: String) = ComposeUIViewController(
    configure = {
        startKoin {
            modules(
                appModule(apiKey),
                module { single { LocationProvider() } }
            )
        }
    }
) { App() }
