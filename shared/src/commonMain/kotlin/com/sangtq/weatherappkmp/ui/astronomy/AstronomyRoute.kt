package com.sangtq.weatherappkmp.ui.astronomy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangtq.weatherappkmp.domain.model.AstronomyDetail
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import com.sangtq.weatherappkmp.ui.components.WeatherErrorScreen
import com.sangtq.weatherappkmp.util.addDaysToIsoDate
import com.sangtq.weatherappkmp.util.humanReadableIsoDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AstronomyRoute(
    location: String,
    onBackClick: () -> Unit,
    viewModel: AstronomyViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val date by viewModel.selectedDate.collectAsStateWithLifecycle()

    LaunchedEffect(location) { viewModel.load(location) }

    AstronomyScreen(
        state = state,
        date = date,
        onBackClick = onBackClick,
        onPreviousDay = { viewModel.setDate(addDaysToIsoDate(date, -1)) },
        onNextDay = { viewModel.setDate(addDaysToIsoDate(date, 1)) },
        onRetry = { viewModel.load(location) }
    )
}

@Composable
fun AstronomyScreen(
    state: Resource<AstronomyDetail>,
    date: String,
    onBackClick: () -> Unit,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onRetry: () -> Unit
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF1A237E), Color(0xFF311B92), Color(0xFF4527A0))
    )
    Column(
        modifier = Modifier.fillMaxSize().background(gradient)
    ) {
        TopBar(onBackClick = onBackClick)
        DateSelector(date = date, onPrev = onPreviousDay, onNext = onNextDay)
        Spacer(modifier = Modifier.height(16.dp))

        when (val s = state) {
            Resource.Loading -> LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.2f)
            )
            is Resource.Error -> WeatherErrorScreen(message = s.message, onRetry = { onRetry() })
            is Resource.Success -> AstronomyContent(detail = s.data)
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
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.15f))
                .clickable(onClick = onBackClick)
                .padding(10.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text("Astronomy", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight(600))
    }
}

@Composable
private fun DateSelector(date: String, onPrev: () -> Unit, onNext: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.15f)).clickable(onClick = onPrev).padding(8.dp)
        ) { Icon(Icons.Default.ChevronLeft, null, tint = Color.White) }
        Text(
            text = humanReadableIsoDate(date),
            color = Color.White, fontSize = 16.sp, fontWeight = FontWeight(500)
        )
        Box(
            modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.15f)).clickable(onClick = onNext).padding(8.dp)
        ) { Icon(Icons.Default.ChevronRight, null, tint = Color.White) }
    }
}

@Composable
private fun AstronomyContent(detail: AstronomyDetail) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${detail.locationName}, ${detail.country}",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(20.dp))

        SunCard(detail = detail)
        Spacer(modifier = Modifier.height(16.dp))
        MoonCard(detail = detail)
    }
}

@Composable
private fun SunCard(detail: AstronomyDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.WbSunny, null, tint = Color(0xFFFFD54F), modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text("Sun", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight(700))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TimeBlock("Sunrise", detail.sunrise)
            TimeBlock("Sunset", detail.sunset)
        }
        if (detail.isSunUp) {
            Spacer(modifier = Modifier.height(12.dp))
            StatusChip("Sun is up", Color(0xFFFFD54F))
        }
    }
}

@Composable
private fun MoonCard(detail: AstronomyDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.NightsStay, null, tint = Color(0xFFB39DDB), modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text("Moon", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight(700))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TimeBlock("Moonrise", detail.moonrise)
            TimeBlock("Moonset", detail.moonset)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            InfoBlock("Phase", detail.moonPhase)
            InfoBlock("Illumination", "${detail.moonIllumination}%")
        }
        if (detail.isMoonUp) {
            Spacer(modifier = Modifier.height(12.dp))
            StatusChip("Moon is up", Color(0xFFB39DDB))
        }
    }
}

@Composable
private fun TimeBlock(label: String, time: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(time.ifBlank { "—" }, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight(700))
    }
}

@Composable
private fun InfoBlock(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value.ifBlank { "—" }, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight(600), textAlign = TextAlign.Center)
    }
}

@Composable
private fun StatusChip(text: String, tint: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(tint.copy(alpha = 0.25f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, color = tint, fontSize = 12.sp, fontWeight = FontWeight(600))
    }
}
