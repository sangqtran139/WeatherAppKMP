package com.sangtq.weatherappkmp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sangtq.weatherappkmp.model.NoteDetailWeather

@Preview
@Composable
private fun WeatherInfoBottomSheetPrecipitationPreview() {
    WeatherInfoBottomSheet(
        noteType = NoteDetailWeather.PRECIPITATION.name,
        onDismiss = {}
    )
}

@Preview
@Composable
private fun WeatherInfoBottomSheetWindPreview() {
    WeatherInfoBottomSheet(
        noteType = NoteDetailWeather.WINDY.name,
        onDismiss = {}
    )
}

@Preview
@Composable
private fun WeatherInfoBottomSheetHumidityPreview() {
    WeatherInfoBottomSheet(
        noteType = NoteDetailWeather.HUMIDITY.name,
        onDismiss = {}
    )
}

@Preview
@Composable
private fun WeatherInfoBottomSheetUVPreview() {
    WeatherInfoBottomSheet(
        noteType = NoteDetailWeather.INDEX_UV.name,
        onDismiss = {}
    )
}
