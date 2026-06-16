package com.sangtq.weatherappkmp.ui.astronomy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
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
import com.sangtq.weatherappkmp.domain.model.AstronomyDetail
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import com.sangtq.weatherappkmp.ui.components.WeatherErrorScreen
import com.sangtq.weatherappkmp.util.addDaysToIsoDate
import com.sangtq.weatherappkmp.util.humanReadableIsoDate
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import weatherappkmp.shared.generated.resources.Res
import weatherappkmp.shared.generated.resources.astronomy_moon_illuminated
import weatherappkmp.shared.generated.resources.astronomy_moon_up_now
import weatherappkmp.shared.generated.resources.astronomy_moonrise
import weatherappkmp.shared.generated.resources.astronomy_moonset
import weatherappkmp.shared.generated.resources.astronomy_sun
import weatherappkmp.shared.generated.resources.astronomy_title
import weatherappkmp.shared.generated.resources.astronomy_up
import weatherappkmp.shared.generated.resources.detail_sunrise
import weatherappkmp.shared.generated.resources.detail_sunset
import weatherappkmp.shared.generated.resources.empty_dash

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
        colors = listOf(Color(0xFF0B1026), Color(0xFF1A1B3A), Color(0xFF2D1B4E))
    )
    Box(modifier = Modifier.fillMaxSize().background(gradient)) {
        StarrySky(modifier = Modifier.fillMaxSize())
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(onBackClick = onBackClick)
            DateSelector(date = date, onPrev = onPreviousDay, onNext = onNextDay)
            Spacer(modifier = Modifier.height(8.dp))

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
        Text(stringResource(Res.string.astronomy_title), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight(600))
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
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${detail.locationName}, ${detail.country}",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        MoonHero(detail = detail)

        Spacer(modifier = Modifier.height(20.dp))

        MoonStatsCard(detail = detail)

        Spacer(modifier = Modifier.height(16.dp))

        SunCard(detail = detail)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun MoonHero(detail: AstronomyDetail) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.1f),
        contentAlignment = Alignment.Center
    ) {
        MoonIllustration(
            phaseName = detail.moonPhase,
            illumination = detail.moonIllumination,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val dashLabel = stringResource(Res.string.empty_dash)
            Text(
                text = detail.moonPhase.ifBlank { dashLabel },
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight(700)
            )
            Text(
                text = stringResource(Res.string.astronomy_moon_illuminated, detail.moonIllumination),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 13.sp
            )
            if (detail.isMoonUp) {
                Spacer(modifier = Modifier.padding(top = 20.dp).height(8.dp))
                StatusChip(stringResource(Res.string.astronomy_moon_up_now), Color(0xFFB39DDB))
            } else {
                Spacer(modifier = Modifier.padding(top = 10.dp).height(30.dp))
            }
        }
    }
}

@Composable
private fun MoonStatsCard(detail: AstronomyDetail) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TimeBlock(stringResource(Res.string.astronomy_moonrise), detail.moonrise)
        VerticalDivider()
        TimeBlock(stringResource(Res.string.astronomy_moonset), detail.moonset)
    }
}

@Composable
private fun SunCard(detail: AstronomyDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.WbSunny, null, tint = Color(0xFFFFD54F), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(stringResource(Res.string.astronomy_sun), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight(700))
            if (detail.isSunUp) {
                Spacer(modifier = Modifier.width(10.dp))
                StatusChip(stringResource(Res.string.astronomy_up), Color(0xFFFFD54F))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            TimeBlock(stringResource(Res.string.detail_sunrise), detail.sunrise, icon = Icons.Default.WbTwilight, tint = Color(0xFFFFB74D))
            VerticalDivider()
            TimeBlock(stringResource(Res.string.detail_sunset), detail.sunset, icon = Icons.Default.WbTwilight, tint = Color(0xFFFF7043))
        }
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(Color.White.copy(alpha = 0.2f))
    )
}

@Composable
private fun TimeBlock(
    label: String,
    time: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    tint: Color = Color.White
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (icon != null) {
            Icon(icon, null, tint = tint, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(4.dp))
        }
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(time.ifBlank { stringResource(Res.string.empty_dash) }, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight(700))
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
