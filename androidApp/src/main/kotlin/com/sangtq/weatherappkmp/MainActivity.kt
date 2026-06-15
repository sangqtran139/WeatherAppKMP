package com.sangtq.weatherappkmp

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sangtq.weatherappkmp.di.initKoin

class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            application = this,
            apiKey = BuildConfig.WEATHER_API_KEY,
            isDebug = BuildConfig.DEBUG
        )
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}
