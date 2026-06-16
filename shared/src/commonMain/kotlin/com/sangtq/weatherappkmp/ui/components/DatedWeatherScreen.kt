package com.sangtq.weatherappkmp.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import com.sangtq.weatherappkmp.util.humanReadableIsoDate
import kotlin.math.roundToInt

@Composable
fun DatedWeatherScreen(
    title: String,
    state: Resource<WeatherData>,
    date: String,
    gradient: List<Color>,
    onBackClick: () -> Unit,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onRetry: () -> Unit,
    canGoPrev: Boolean = true,
    canGoNext: Boolean = true
) {
    Column(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(gradient))) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp, top = 36.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.15f)).clickable(onClick = onBackClick).padding(10.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight(600))
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val prevAlpha = if (canGoPrev) 1f else 0.3f
            val nextAlpha = if (canGoNext) 1f else 0.3f
            Box(modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.15f * prevAlpha)).clickable(enabled = canGoPrev, onClick = onPreviousDay).padding(8.dp)) {
                Icon(Icons.Default.ChevronLeft, null, tint = Color.White.copy(alpha = prevAlpha))
            }
            Text(humanReadableIsoDate(date), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight(500))
            Box(modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.15f * nextAlpha)).clickable(enabled = canGoNext, onClick = onNextDay).padding(8.dp)) {
                Icon(Icons.Default.ChevronRight, null, tint = Color.White.copy(alpha = nextAlpha))
            }
        }

        when (val s = state) {
            Resource.Loading -> LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.2f)
            )
            is Resource.Error -> WeatherErrorScreen(message = s.message, onRetry = { onRetry() })
            is Resource.Success -> DatedWeatherContent(data = s.data)
        }
    }
}

@Composable
private fun DatedWeatherContent(data: WeatherData) {
    val day = data.forecastDays.firstOrNull() ?: return
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "${data.location.name}, ${data.location.country}",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp
            )
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.12f))
                    .padding(20.dp)
            ) {
                Text(
                    text = "${day.day.avgTempC.roundToInt()}°",
                    color = Color.White, fontSize = 64.sp, fontWeight = FontWeight(700)
                )
                Text(
                    text = day.day.condition.text.ifBlank { "—" },
                    color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp
                )
                Text(
                    text = "Max ${day.day.maxTempC.roundToInt()}° · Min ${day.day.minTempC.roundToInt()}°",
                    color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp
                )
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatTile(Icons.Default.WaterDrop, "Rain chance", "${day.day.dailyChanceOfRain}%", modifier = Modifier.weight(1f))
                StatTile(Icons.Default.Air, "Max wind", "${day.day.maxWindKph.roundToInt()} km/h", modifier = Modifier.weight(1f))
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatTile(Icons.Default.Opacity, "Humidity", "${day.day.avgHumidity}%", modifier = Modifier.weight(1f))
                StatTile(Icons.Default.WbSunny, "UV index", "${day.day.uv}", modifier = Modifier.weight(1f))
            }
        }
        if (day.astro.sunrise.isNotBlank()) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .padding(16.dp)
                ) {
                    Text("Sun & moon", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight(700))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        AstroLabel("Sunrise", day.astro.sunrise)
                        AstroLabel("Sunset", day.astro.sunset)
                        AstroLabel("Moonrise", day.astro.moonrise)
                        AstroLabel("Moonset", day.astro.moonset)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatTile(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Color.White.copy(alpha = 0.85f), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight(700))
    }
}

@Composable
private fun AstroLabel(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value.ifBlank { "—" }, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight(600))
    }
}
