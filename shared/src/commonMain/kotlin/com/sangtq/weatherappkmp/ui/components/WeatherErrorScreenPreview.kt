package com.sangtq.weatherappkmp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun WeatherErrorScreenPreview() {
    WeatherErrorScreen(
        message = "Unable to connect to the server. Please check your internet connection.",
        onRetry = {}
    )
}
