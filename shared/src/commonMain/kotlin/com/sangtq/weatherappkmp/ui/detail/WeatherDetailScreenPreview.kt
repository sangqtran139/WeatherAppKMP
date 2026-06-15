package com.sangtq.weatherappkmp.ui.detail

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sangtq.weatherappkmp.ui.preview.previewAstro
import com.sangtq.weatherappkmp.ui.preview.previewForecastDay
import com.sangtq.weatherappkmp.ui.preview.previewHourWeather

@Preview(showBackground = true)
@Composable
private fun DetailHourItemPreview() {
    DetailHourItem(hour = previewHourWeather, isCurrentHour = false)
}

@Preview(showBackground = true)
@Composable
private fun DetailHourItemCurrentPreview() {
    DetailHourItem(hour = previewHourWeather, isCurrentHour = true)
}

@Preview(showBackground = true)
@Composable
private fun DetailHourWeatherListPreview() {
    DetailHourWeatherList(
        state = rememberLazyListState(),
        forecastDay = previewForecastDay,
        hourNow = 14,
        onClickChooseHour = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun SunriseSunsetCardPreview() {
    SunriseSunsetCard(
        sunrise = previewAstro.sunrise,
        sunset = previewAstro.sunset,
        currentHourOfDay = 14
    )
}
