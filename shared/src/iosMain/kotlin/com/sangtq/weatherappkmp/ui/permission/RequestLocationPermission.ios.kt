package com.sangtq.weatherappkmp.ui.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
actual fun RequestLocationPermission(onGranted: () -> Unit, onDenied: () -> Unit) {
    // iOS permission is handled inside LocationProvider via CLLocationManager.requestWhenInUseAuthorization()
    LaunchedEffect(Unit) { onGranted() }
}
