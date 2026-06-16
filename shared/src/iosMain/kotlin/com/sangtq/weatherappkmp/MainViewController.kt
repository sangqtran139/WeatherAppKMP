package com.sangtq.weatherappkmp

import androidx.compose.ui.window.ComposeUIViewController
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import com.sangtq.weatherappkmp.di.appModule
import com.sangtq.weatherappkmp.platform.LocationProvider
import org.koin.core.context.startKoin
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

fun MainViewController(apiKey: String) = ComposeUIViewController(
    configure = {
        startKoin {
            modules(
                appModule(apiKey),
                module {
                    single { LocationProvider() }
                    single<Settings> { NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults) }
                }
            )
        }
    }
) { App() }
