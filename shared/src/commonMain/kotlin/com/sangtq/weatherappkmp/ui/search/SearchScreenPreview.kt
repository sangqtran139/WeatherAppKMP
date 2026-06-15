package com.sangtq.weatherappkmp.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import com.sangtq.weatherappkmp.ui.preview.previewSearchLocations
import com.sangtq.weatherappkmp.ui.preview.previewWeatherData

@Preview(showBackground = true)
@Composable
private fun SearchScreenEmptyPreview() {
    SearchScreen(
        searchQuery = "",
        searchResults = Resource.Success(emptyList()),
        currentWeather = previewWeatherData,
        onQueryChange = {},
        onBackClick = {},
        onSelectLocation = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenWithResultsPreview() {
    SearchScreen(
        searchQuery = "Ha",
        searchResults = Resource.Success(previewSearchLocations),
        currentWeather = previewWeatherData,
        onQueryChange = {},
        onBackClick = {},
        onSelectLocation = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenLoadingPreview() {
    SearchScreen(
        searchQuery = "Ha",
        searchResults = Resource.Loading,
        currentWeather = previewWeatherData,
        onQueryChange = {},
        onBackClick = {},
        onSelectLocation = {}
    )
}
