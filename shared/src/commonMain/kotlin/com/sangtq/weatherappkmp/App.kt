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
import com.sangtq.weatherappkmp.ui.astronomy.AstronomyRoute
import com.sangtq.weatherappkmp.ui.detail.WeatherDetailRoute
import com.sangtq.weatherappkmp.ui.future.FutureRoute
import com.sangtq.weatherappkmp.ui.history.HistoryRoute
import com.sangtq.weatherappkmp.ui.home.WeatherHomeRoute
import com.sangtq.weatherappkmp.ui.marine.MarineRoute
import com.sangtq.weatherappkmp.ui.search.SearchRoute
import com.sangtq.weatherappkmp.ui.sports.SportsRoute

sealed class Screen {
    object Home : Screen()
    object Detail : Screen()
    object Search : Screen()
    object Astronomy : Screen()
    object Marine : Screen()
    object Sports : Screen()
    object History : Screen()
    object Future : Screen()
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
            var permissionHandled by remember { mutableStateOf(false) }
            val goHome: () -> Unit = { currentScreen = Screen.Home }

            when (currentScreen) {
                is Screen.Home -> WeatherHomeRoute(
                    modifier = Modifier,
                    location = currentLocation,
                    permissionHandled = permissionHandled,
                    onPermissionHandled = { permissionHandled = true },
                    onLocationDetected = { currentLocation = it },
                    openWeatherDetail = { currentScreen = Screen.Detail },
                    openSearch = { currentScreen = Screen.Search },
                    openFeature = { feature -> currentScreen = feature }
                )
                is Screen.Detail -> WeatherDetailRoute(
                    modifier = Modifier,
                    location = currentLocation,
                    onBackClick = goHome
                )
                is Screen.Search -> SearchRoute(
                    modifier = Modifier,
                    onBackClick = goHome,
                    onSelectLocation = { location ->
                        currentLocation = location
                        permissionHandled = true
                        currentScreen = Screen.Home
                    }
                )
                is Screen.Astronomy -> AstronomyRoute(location = currentLocation, onBackClick = goHome)
                is Screen.Marine -> MarineRoute(location = currentLocation, onBackClick = goHome)
                is Screen.Sports -> SportsRoute(onBackClick = goHome)
                is Screen.History -> HistoryRoute(location = currentLocation, onBackClick = goHome)
                is Screen.Future -> FutureRoute(location = currentLocation, onBackClick = goHome)
            }
        }
    }
}
