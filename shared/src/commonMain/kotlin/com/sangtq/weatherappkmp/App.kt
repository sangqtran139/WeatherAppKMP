package com.sangtq.weatherappkmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.sangtq.weatherappkmp.ui.detail.WeatherDetailRoute
import com.sangtq.weatherappkmp.ui.home.WeatherHomeRoute
import com.sangtq.weatherappkmp.ui.search.SearchRoute

sealed class Screen {
    object Home : Screen()
    object Detail : Screen()
    object Search : Screen()
}

@Composable
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components { add(KtorNetworkFetcherFactory()) }
            .build()
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
            var currentLocation by remember { mutableStateOf("Vietnam") }

            when (currentScreen) {
                is Screen.Home -> WeatherHomeRoute(
                    modifier = Modifier,
                    location = currentLocation,
                    onLocationDetected = { currentLocation = it },
                    openWeatherDetail = { currentScreen = Screen.Detail },
                    openSearch = { currentScreen = Screen.Search }
                )
                is Screen.Detail -> WeatherDetailRoute(
                    modifier = Modifier,
                    location = currentLocation,
                    onBackClick = { currentScreen = Screen.Home }
                )
                is Screen.Search -> SearchRoute(
                    modifier = Modifier,
                    onBackClick = { currentScreen = Screen.Home },
                    onSelectLocation = { location ->
                        currentLocation = location
                        currentScreen = Screen.Home
                    }
                )
            }
        }
    }
}
