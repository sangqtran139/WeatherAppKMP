package com.sangtq.weatherappkmp.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangtq.weatherappkmp.domain.model.FavoriteCity
import com.sangtq.weatherappkmp.domain.model.SearchLocation
import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import com.sangtq.weatherappkmp.ui.components.WeatherIcon
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import weatherappkmp.shared.generated.resources.Res
import weatherappkmp.shared.generated.resources.action_back
import weatherappkmp.shared.generated.resources.action_clear
import weatherappkmp.shared.generated.resources.search_favorites
import weatherappkmp.shared.generated.resources.search_helper
import weatherappkmp.shared.generated.resources.search_hint
import weatherappkmp.shared.generated.resources.search_no_results
import weatherappkmp.shared.generated.resources.search_recent
import weatherappkmp.shared.generated.resources.unit_celsius
import kotlin.math.roundToInt

@Composable
fun SearchRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onSelectLocation: (String) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults: Resource<List<SearchLocation>> by viewModel.searchResults.collectAsStateWithLifecycle()
    val currentWeather: WeatherData? by viewModel.currentWeather.collectAsStateWithLifecycle()
    val timezones: Map<Int, String> by viewModel.timezones.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val recent by viewModel.recent.collectAsStateWithLifecycle()

    SearchScreen(
        modifier = modifier,
        searchQuery = searchQuery,
        searchResults = searchResults,
        currentWeather = currentWeather,
        timezones = timezones,
        favorites = favorites,
        recent = recent,
        onQueryChange = viewModel::onQueryChange,
        onBackClick = onBackClick,
        onSelectLocation = { query ->
            viewModel.recordSelection(query)
            onSelectLocation(query)
        },
        onRemoveFavorite = viewModel::removeFavorite,
        onClearRecent = viewModel::clearRecent
    )
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchQuery: String,
    searchResults: Resource<List<SearchLocation>>,
    currentWeather: WeatherData?,
    timezones: Map<Int, String> = emptyMap(),
    favorites: List<FavoriteCity> = emptyList(),
    recent: List<String> = emptyList(),
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSelectLocation: (String) -> Unit,
    onRemoveFavorite: (FavoriteCity) -> Unit = {},
    onClearRecent: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
            .padding(horizontal = 12.dp, vertical = 16.dp)
    ) {
        // Search bar
        Row(
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.action_back),
                    tint = Color(0xFF333333)
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                if (searchQuery.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.search_hint),
                        color = Color(0xFFBDBDBD),
                        fontSize = 16.sp
                    )
                }
                BasicTextField(
                    value = searchQuery,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(fontSize = 16.sp, color = Color(0xFF333333)),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(Res.string.search_helper),
            color = Color(0xFF828282),
            fontSize = 12.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        if (currentWeather != null) {
            CurrentLocationItem(weather = currentWeather)
        }

        if (searchQuery.isEmpty()) {
            if (favorites.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                SectionHeader(title = stringResource(Res.string.search_favorites))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                ) {
                    favorites.forEachIndexed { index, fav ->
                        FavoriteRow(
                            city = fav,
                            onClick = { onSelectLocation(fav.query) },
                            onRemove = { onRemoveFavorite(fav) })
                        if (index < favorites.size - 1) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color(0xFFE0E0E0),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
            if (recent.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SectionHeader(title = stringResource(Res.string.search_recent), modifier = Modifier.weight(1f))
                    Text(
                        text = stringResource(Res.string.action_clear),
                        color = Color(0xFF2F80ED),
                        fontSize = 12.sp,
                        fontWeight = FontWeight(600),
                        modifier = Modifier.clickable(onClick = onClearRecent).padding(end = 8.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                ) {
                    recent.forEachIndexed { index, q ->
                        RecentRow(query = q, onClick = { onSelectLocation(q) })
                        if (index < recent.size - 1) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color(0xFFE0E0E0),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }

        if (searchQuery.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            when (searchResults) {
                Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color(0xFF2F80ED),
                            strokeWidth = 2.dp
                        )
                    }
                }

                is Resource.Error -> {
                    Text(
                        text = searchResults.message,
                        color = Color(0xFFEB5757),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                is Resource.Success -> {
                    val locations = searchResults.data
                    if (locations.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.search_no_results),
                            color = Color(0xFF828282),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                        ) {
                            items(locations.size) { index ->
                                SearchResultItem(
                                    location = locations[index],
                                    localTime = timezones[locations[index].id],
                                    onClick = { onSelectLocation(locations[index].name) }
                                )
                                if (index < locations.size - 1) {
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = Color(0xFFE0E0E0),
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentLocationItem(weather: WeatherData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.LocationOn,
            null,
            tint = Color(0xFFEB5757),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${weather.location.name}, ${weather.location.country}",
            color = Color(0xFF333333), fontSize = 14.sp, fontWeight = FontWeight(500),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = stringResource(Res.string.unit_celsius, weather.current.tempC.roundToInt()),
            color = Color(0xFF333333),
            fontSize = 14.sp,
            fontWeight = FontWeight(500)
        )
        Spacer(modifier = Modifier.width(8.dp))
        WeatherIcon(iconUrl = weather.current.condition.icon, modifier = Modifier.size(28.dp))
    }
}

@Composable
private fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        color = Color(0xFF828282),
        fontSize = 12.sp,
        fontWeight = FontWeight(500),
        modifier = modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
private fun FavoriteRow(city: FavoriteCity, onClick: () -> Unit, onRemove: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Star, null, tint = Color(0xFFF2C94C), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                city.name,
                color = Color(0xFF333333),
                fontSize = 14.sp,
                fontWeight = FontWeight(600)
            )
            if (city.country.isNotEmpty()) {
                Text(city.country, color = Color(0xFF828282), fontSize = 12.sp)
            }
        }
        Box(modifier = Modifier.clickable(onClick = onRemove).padding(6.dp)) {
            Icon(
                Icons.Default.Close,
                null,
                tint = Color(0xFF828282),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun RecentRow(query: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.History, null, tint = Color(0xFF828282), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(query, color = Color(0xFF333333), fontSize = 14.sp)
    }
}

@Composable
private fun SearchResultItem(
    location: SearchLocation,
    localTime: String?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = Color(0xFF2F80ED),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = location.name,
                color = Color(0xFF333333),
                fontSize = 14.sp,
                fontWeight = FontWeight(600)
            )
            val subtitle = listOf(location.region, location.country).filter { it.isNotEmpty() }
                .joinToString(", ")
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    color = Color(0xFF828282),
                    fontSize = 12.sp
                )
            }
            if (!localTime.isNullOrEmpty()) {
                Text(
                    text = localTime,
                    color = Color(0xFF2F80ED),
                    fontSize = 11.sp,
                    fontWeight = FontWeight(500)
                )
            }
        }
    }
}
