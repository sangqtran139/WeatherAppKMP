package com.sangtq.weatherappkmp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sangtq.weatherappkmp.ui.preview.previewForecastDay
import com.sangtq.weatherappkmp.ui.preview.previewHourWeather
import com.sangtq.weatherappkmp.ui.preview.previewWeatherData

@Preview(showBackground = true)
@Composable
private fun DetailWeatherTodayDayPreview() {
    DetailWeatherToday(hourNow = 14, weatherData = previewWeatherData)
}

@Preview(showBackground = true)
@Composable
private fun DetailWeatherTodayNightPreview() {
    DetailWeatherToday(hourNow = 23, weatherData = previewWeatherData)
}

@Preview(showBackground = true)
@Composable
private fun HourlyWeatherItemPreview() {
    HourlyWeatherItem(hour = previewHourWeather, isCurrentHour = false)
}

@Preview(showBackground = true)
@Composable
private fun HourlyWeatherItemCurrentPreview() {
    HourlyWeatherItem(hour = previewHourWeather, isCurrentHour = true)
}

@Preview(showBackground = true)
@Composable
private fun HourWeatherLazyListPreview() {
    HourWeatherLazyList(
        state = rememberLazyListState(),
        forecastDay = previewForecastDay,
        contentPaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        spaceBy = Arrangement.spacedBy(8.dp),
        hourNow = 14,
        onClickChooseHour = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun WeatherForecastItemPreview() {
    WeatherForecastItem(forecastDay = previewForecastDay, onClickTips = {})
}

@Preview(showBackground = true)
@Composable
private fun DetailWeatherForecastForDayPreview() {
    DetailWeatherForecastForDay(forecastDay = previewForecastDay, onClickTips = {})
}
