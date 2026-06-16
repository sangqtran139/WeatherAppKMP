package com.sangtq.weatherappkmp.ui.marine

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Tsunami
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangtq.weatherappkmp.domain.model.MarineDay
import com.sangtq.weatherappkmp.domain.model.MarineForecast
import com.sangtq.weatherappkmp.domain.model.MarineHour
import com.sangtq.weatherappkmp.domain.model.MarineTide
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import com.sangtq.weatherappkmp.ui.components.WeatherErrorScreen
import com.sangtq.weatherappkmp.util.convertEpochToHour
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import weatherappkmp.shared.generated.resources.Res
import weatherappkmp.shared.generated.resources.marine_day_overview
import weatherappkmp.shared.generated.resources.marine_hourly_swell
import weatherappkmp.shared.generated.resources.marine_tides
import weatherappkmp.shared.generated.resources.marine_title
import weatherappkmp.shared.generated.resources.metric_max_temp
import weatherappkmp.shared.generated.resources.metric_max_wind
import weatherappkmp.shared.generated.resources.metric_min_temp
import weatherappkmp.shared.generated.resources.unit_celsius
import weatherappkmp.shared.generated.resources.unit_kmh
import weatherappkmp.shared.generated.resources.unit_meters
import weatherappkmp.shared.generated.resources.unit_water_temp
import kotlin.math.roundToInt

@Composable
fun MarineRoute(
    location: String,
    onBackClick: () -> Unit,
    viewModel: MarineViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedDay by viewModel.selectedDayIndex.collectAsStateWithLifecycle()

    LaunchedEffect(location) { viewModel.load(location) }

    MarineScreen(
        state = state,
        selectedDay = selectedDay,
        onBackClick = onBackClick,
        onSelectDay = viewModel::selectDay,
        onRetry = viewModel::retry
    )
}

@Composable
fun MarineScreen(
    state: Resource<MarineForecast>,
    selectedDay: Int,
    onBackClick: () -> Unit,
    onSelectDay: (Int) -> Unit,
    onRetry: () -> Unit
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF006064), Color(0xFF00838F), Color(0xFF0097A7))
    )
    Column(modifier = Modifier.fillMaxSize().background(gradient)) {
        TopBar(onBackClick = onBackClick)
        when (val s = state) {
            Resource.Loading -> LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                color = Color.White
            )
            is Resource.Error -> WeatherErrorScreen(message = s.message, onRetry = { onRetry() })
            is Resource.Success -> MarineContent(forecast = s.data, selectedDay = selectedDay, onSelectDay = onSelectDay)
        }
    }
}

@Composable
private fun TopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp, top = 36.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.15f)).clickable(onClick = onBackClick).padding(10.dp)
        ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(20.dp)) }
        Spacer(modifier = Modifier.width(12.dp))
        Text(stringResource(Res.string.marine_title), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight(600))
    }
}

@Composable
private fun MarineContent(forecast: MarineForecast, selectedDay: Int, onSelectDay: (Int) -> Unit) {
    val safeIndex = selectedDay.coerceIn(0, (forecast.days.size - 1).coerceAtLeast(0))
    val day = forecast.days.getOrNull(safeIndex)

    LazyColumn(contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text(
                text = "${forecast.locationName}, ${forecast.country}",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            DayTabs(days = forecast.days, selectedIndex = safeIndex, onSelect = onSelectDay)
        }
        if (day != null) {
            item { SummaryCard(day) }
            if (day.tides.isNotEmpty()) item { TidesCard(day.tides) }
            if (day.hours.isNotEmpty()) item { HourlyCard(day.hours, forecast.timezoneId) }
        }
    }
}

@Composable
private fun DayTabs(days: List<MarineDay>, selectedIndex: Int, onSelect: (Int) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(days) { idx, day ->
            val isSelected = idx == selectedIndex
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.15f))
                    .clickable { onSelect(idx) }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = day.date.substringAfter("-").replace("-", "/"),
                    color = if (isSelected) Color(0xFF006064) else Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight(600)
                )
            }
        }
    }
}

@Composable
private fun SummaryCard(day: MarineDay) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .padding(16.dp)
    ) {
        Text(stringResource(Res.string.marine_day_overview), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight(700))
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Stat(stringResource(Res.string.metric_max_temp), stringResource(Res.string.unit_celsius, day.maxTempC.roundToInt()))
            Stat(stringResource(Res.string.metric_min_temp), stringResource(Res.string.unit_celsius, day.minTempC.roundToInt()))
            Stat(stringResource(Res.string.metric_max_wind), stringResource(Res.string.unit_kmh, day.maxWindKph.roundToInt()))
        }
    }
}

@Composable
private fun TidesCard(tides: List<MarineTide>) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Waves, null, tint = Color(0xFF80DEEA), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(Res.string.marine_tides), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight(700))
        }
        Spacer(modifier = Modifier.height(8.dp))
        tides.forEach { tide ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(tide.time, color = Color.White, fontSize = 13.sp)
                Text(tide.type, color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
                Text(stringResource(Res.string.unit_meters, tide.heightMeters.toString()), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight(600))
            }
        }
    }
}

@Composable
private fun HourlyCard(hours: List<MarineHour>, timezoneId: String) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .padding(vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
            Icon(Icons.Default.Tsunami, null, tint = Color(0xFF80DEEA), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(Res.string.marine_hourly_swell), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight(700))
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(hours.filterIndexed { idx, _ -> idx % 2 == 0 }) { hour ->
                MarineHourItem(hour, timezoneId)
            }
        }
    }
}

@Composable
private fun MarineHourItem(hour: MarineHour, timezoneId: String) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("${convertEpochToHour(hour.timeEpoch, timezoneId)}:00", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight(600))
        Spacer(modifier = Modifier.height(4.dp))
        Text(stringResource(Res.string.unit_meters, hour.swellHtMeters.toString()), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight(700))
        Text(hour.swellDir, color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Air, null, tint = Color.White, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(2.dp))
            Text("${hour.windKph.roundToInt()}", color = Color.White, fontSize = 11.sp)
        }
        Text(stringResource(Res.string.unit_water_temp, hour.waterTempC.roundToInt()), color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
    }
}

@Composable
private fun Stat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight(700))
    }
}
