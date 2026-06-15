package com.sangtq.weatherappkmp.ui.permission

import androidx.compose.runtime.Composable

@Composable
expect fun RequestLocationPermission(onGranted: () -> Unit, onDenied: () -> Unit)
