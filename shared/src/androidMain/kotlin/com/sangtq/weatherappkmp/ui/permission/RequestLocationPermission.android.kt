package com.sangtq.weatherappkmp.ui.permission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
actual fun RequestLocationPermission(onGranted: () -> Unit, onDenied: () -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) onGranted() else onDenied()
    }
    LaunchedEffect(Unit) {
        launcher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    }
}
