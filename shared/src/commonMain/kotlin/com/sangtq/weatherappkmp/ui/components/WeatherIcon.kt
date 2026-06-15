package com.sangtq.weatherappkmp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun WeatherIcon(
    iconUrl: String,
    modifier: Modifier = Modifier
) {
    val url = iconUrl.toWeatherIconUrl()
    val fallback = rememberVectorPainter(image = Icons.Default.WaterDrop)
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit,
        placeholder = fallback,
        error = fallback
    )
}

fun String.toWeatherIconUrl(): String {
    if (isEmpty()) return ""
    val withProtocol = if (startsWith("//")) "https:$this" else this
    return withProtocol.replace("/64x64/", "/128x128/")
}
