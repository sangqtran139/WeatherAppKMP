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
import androidx.compose.material.icons.filled.LocationOn
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
import com.sangtq.weatherappkmp.domain.model.SearchLocation
import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import com.sangtq.weatherappkmp.ui.components.WeatherIcon
import org.koin.compose.viewmodel.koinViewModel
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

    SearchScreen(
        modifier = modifier,
        searchQuery = searchQuery,
        searchResults = searchResults,
        currentWeather = currentWeather,
        onQueryChange = viewModel::onQueryChange,
        onBackClick = onBackClick,
        onSelectLocation = onSelectLocation
    )
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchQuery: String,
    searchResults: Resource<List<SearchLocation>>,
    currentWeather: WeatherData?,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSelectLocation: (String) -> Unit
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
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF333333)
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                if (searchQuery.isEmpty()) {
                    Text(
                        text = "Cari alamat",
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
            text = "Lokasi sekarang",
            color = Color(0xFF828282),
            fontSize = 12.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        if (currentWeather != null) {
            CurrentLocationItem(weather = currentWeather)
        }

        if (searchQuery.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            when (searchResults) {
                Resource.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 12.dp), contentAlignment = Alignment.Center) {
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
                            text = "Không tìm thấy địa điểm",
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
        Icon(Icons.Default.LocationOn, null, tint = Color(0xFFEB5757), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${weather.location.name}, ${weather.location.country}",
            color = Color(0xFF333333), fontSize = 14.sp, fontWeight = FontWeight(500),
            modifier = Modifier.weight(1f)
        )
        Text(text = "${weather.current.tempC.roundToInt()}°C", color = Color(0xFF333333), fontSize = 14.sp, fontWeight = FontWeight(500))
        Spacer(modifier = Modifier.width(8.dp))
        WeatherIcon(iconUrl = weather.current.condition.icon, modifier = Modifier.size(28.dp))
    }
}

@Composable
private fun SearchResultItem(
    location: SearchLocation,
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
        Column {
            Text(
                text = location.name,
                color = Color(0xFF333333),
                fontSize = 14.sp,
                fontWeight = FontWeight(600)
            )
            val subtitle = listOf(location.region, location.country).filter { it.isNotEmpty() }.joinToString(", ")
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    color = Color(0xFF828282),
                    fontSize = 12.sp
                )
            }
        }
    }
}
