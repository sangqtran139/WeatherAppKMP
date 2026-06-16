package com.sangtq.weatherappkmp.ui.detail

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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangtq.weatherappkmp.domain.model.ForecastDay
import com.sangtq.weatherappkmp.domain.model.HourWeather
import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.model.NoteDetailWeather
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import com.sangtq.weatherappkmp.ui.components.AnimatedShimmer
import com.sangtq.weatherappkmp.ui.components.WeatherErrorScreen
import com.sangtq.weatherappkmp.ui.components.WeatherIcon
import com.sangtq.weatherappkmp.ui.components.WeatherInfoBottomSheet
import com.sangtq.weatherappkmp.ui.components.weatherBackgroundColors
import com.sangtq.weatherappkmp.util.convertEpochToHour
import com.sangtq.weatherappkmp.util.convertEpochToLocalDate
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun WeatherDetailRoute(
    modifier: Modifier = Modifier,
    location: String = "Vietnam",
    viewModel: WeatherDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit = {}
) {
    LaunchedEffect(location) {
        viewModel.loadWeather(location)
    }

    val weatherDetailState by viewModel.uiState.collectAsStateWithLifecycle()

    var weatherDetailData by remember { mutableStateOf<WeatherData?>(null) }

    when (val state = weatherDetailState) {
        Resource.Loading -> {
            AnimatedShimmer(); return
        }

        is Resource.Error -> {
            WeatherErrorScreen(message = state.message, onRetry = viewModel::loadWeather); return
        }

        is Resource.Success -> weatherDetailData = state.data
    }

    val weatherData = weatherDetailData ?: return

    val listHourState = rememberLazyListState()
    val hourNow = remember { mutableIntStateOf(0) }
    val backgroundColors = weatherBackgroundColors(hourNow.intValue)
    var showBottomSheet by remember { mutableStateOf(false) }
    var noteTypeForBottomSheet by remember { mutableStateOf("") }

    LaunchedEffect(weatherData) {
        hourNow.intValue =
            convertEpochToHour(weatherData.location.localtimeEpoch, weatherData.location.timezoneId)
        listHourState.animateScrollToItem(
            if (hourNow.intValue > 6) hourNow.intValue - 2 else hourNow.intValue
        )
    }

    val firstDay = weatherData.forecastDays.firstOrNull()
    val currentHour = firstDay?.hours?.getOrNull(hourNow.intValue)

    Column(
        modifier = modifier.fillMaxSize().background(Color(0xFFF2F2F2))
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

            // --- Gradient header card ---
            Column(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                    .background(Brush.verticalGradient(colors = backgroundColors))
                    .padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 24.dp)
            ) {
                // Back + location pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.clickable { onBackClick() }.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.25f))
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${weatherData.location.name}, ${weatherData.location.country}",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(500),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Temp + condition icon side by side
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${currentHour?.tempC?.roundToInt()}°",
                            color = Color.White,
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currentHour?.condition?.text ?: "",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Afternoon ${firstDay?.day?.maxTempC?.roundToInt()}° / Night ${firstDay?.day?.minTempC?.roundToInt()}°",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 13.sp
                        )
                    }
                    WeatherIcon(
                        iconUrl = currentHour?.condition?.icon ?: "",
                        modifier = Modifier.size(72.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Hourly scroll
                if (firstDay != null && firstDay.hours.isNotEmpty()) {
                    DetailHourWeatherList(
                        state = listHourState,
                        forecastDay = firstDay!!,
                        hourNow = hourNow.intValue,
                        timezoneId = weatherData.location.timezoneId,
                        onClickChooseHour = {
                            hourNow.intValue =
                                convertEpochToHour(it.timeEpoch, weatherData.location.timezoneId)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Detail metrics card ---
            if (firstDay != null && currentHour != null) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Detail",
                        color = Color(0xFF828282),
                        fontSize = 12.sp,
                        fontWeight = FontWeight(600)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        DetailMetricItem(
                            modifier = Modifier.weight(1f),
                            icon = {
                                Icon(
                                    Icons.Default.WaterDrop,
                                    null,
                                    tint = Color(0xFF4884DA),
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            label = "Precipitation",
                            value = "${currentHour.chanceOfRain}%",
                            onInfoClick = {
                                noteTypeForBottomSheet =
                                    NoteDetailWeather.PRECIPITATION.name; showBottomSheet = true
                            }
                        )
                        DetailMetricItem(
                            modifier = Modifier.weight(1f),
                            icon = {
                                Icon(
                                    Icons.Default.Air,
                                    null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            label = "Wind",
                            value = "${currentHour.windKph.roundToInt()} km/h",
                            onInfoClick = {
                                noteTypeForBottomSheet =
                                    NoteDetailWeather.WINDY.name; showBottomSheet = true
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        DetailMetricItem(
                            modifier = Modifier.weight(1f),
                            icon = {
                                Icon(
                                    Icons.Default.Opacity,
                                    null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            label = "Humidity",
                            value = "${currentHour.humidity}%",
                            onInfoClick = {
                                noteTypeForBottomSheet =
                                    NoteDetailWeather.HUMIDITY.name; showBottomSheet = true
                            }
                        )
                        DetailMetricItem(
                            modifier = Modifier.weight(1f),
                            icon = {
                                Icon(
                                    Icons.Default.WbSunny,
                                    null,
                                    tint = Color(0xFFFFB300),
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            label = "UV Index",
                            value = "${currentHour.uv.roundToInt()}",
                            onInfoClick = {
                                noteTypeForBottomSheet =
                                    NoteDetailWeather.INDEX_UV.name; showBottomSheet = true
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // --- Siang section ---
                val afternoonHour = firstDay.hours.getOrNull(14)
                val siangDesc = buildString {
                    append(afternoonHour?.condition?.text ?: firstDay.day.condition.text)
                    append(" with high temperature ${firstDay.day.maxTempC.roundToInt()}°C")
                    append(" and precipitation chance ${afternoonHour?.chanceOfRain ?: firstDay.day.dailyChanceOfRain}%.")
                    append(" ${afternoonHour?.windDir ?: ""} winds at ${afternoonHour?.windKph?.roundToInt() ?: firstDay.day.maxWindKph.roundToInt()} km/h,")
                    append(" humidity ${afternoonHour?.humidity ?: firstDay.day.avgHumidity}%.")
                }

                DayNightDescriptionCard(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    isDay = true,
                    description = siangDesc
                )

                Spacer(modifier = Modifier.height(12.dp))

                // --- Malam section ---
                val nightHour = firstDay.hours.getOrNull(21)
                val malamDesc = buildString {
                    append(nightHour?.condition?.text ?: firstDay.day.condition.text)
                    append(" with precipitation chance ${nightHour?.chanceOfRain ?: firstDay.day.dailyChanceOfRain}%.")
                    append(" Low temperature ${firstDay.day.minTempC.roundToInt()}°C.")
                    if (nightHour != null) append(" ${nightHour.windDir} winds at ${nightHour.windKph.roundToInt()} km/h.")
                }

                DayNightDescriptionCard(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    isDay = false,
                    description = malamDesc
                )

                Spacer(modifier = Modifier.height(12.dp))

                // --- Sunrise/Sunset section ---
                SunriseSunsetCard(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    sunrise = firstDay.astro.sunrise,
                    sunset = firstDay.astro.sunset,
                    currentHourOfDay = hourNow.intValue
                )
            }

            Spacer(modifier = Modifier.height(70.dp))
        }
    }

    if (showBottomSheet) {
        WeatherInfoBottomSheet(
            noteType = noteTypeForBottomSheet,
            onDismiss = { showBottomSheet = false }
        )
    }
}

@Composable
private fun DetailMetricItem(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    label: String,
    value: String,
    onInfoClick: () -> Unit
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon()
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = Color(0xFF333333),
                fontSize = 13.sp,
                fontWeight = FontWeight(600)
            )
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.clickable { onInfoClick() }.padding(start = 4.dp).size(14.dp),
                tint = Color(0xFF828282)
            )
        }
        Text(
            text = value,
            color = Color(0xFF333333),
            fontSize = 14.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier.padding(start = 24.dp, top = 2.dp)
        )
    }
}

@Composable
private fun DayNightDescriptionCard(
    modifier: Modifier = Modifier,
    isDay: Boolean,
    description: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isDay) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = null,
                    tint = Color(0xFFFFB300),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Afternoon",
                    color = Color(0xFF333333),
                    fontSize = 14.sp,
                    fontWeight = FontWeight(600)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Nightlight,
                    contentDescription = null,
                    tint = Color(0xFF5C6BC0),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Night",
                    color = Color(0xFF333333),
                    fontSize = 14.sp,
                    fontWeight = FontWeight(600)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            color = Color(0xFF555555),
            fontSize = 13.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
internal fun SunriseSunsetCard(
    modifier: Modifier = Modifier,
    sunrise: String,
    sunset: String,
    currentHourOfDay: Int
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Sunrise & Sunset",
            color = Color(0xFF828282),
            fontSize = 12.sp,
            fontWeight = FontWeight(600)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Arc canvas
        val sunriseHour = parseTimeToHour(sunrise)
        val sunsetHour = parseTimeToHour(sunset)
        val progress = ((currentHourOfDay - sunriseHour) / (sunsetHour - sunriseHour))
            .coerceIn(0f, 1f)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
        ) {
            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val cx = w / 2f
                val cy = h  // bottom center
                val rx = w * 0.45f
                val ry = h * 0.95f

                // Arc background fill
                drawArc(
                    color = Color(0xFFE3F2FD),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(cx - rx, cy - ry),
                    size = Size(rx * 2, ry * 2)
                )
                // Arc stroke
                drawArc(
                    color = Color(0xFF90CAF9),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    style = Stroke(width = 2.dp.toPx()),
                    topLeft = Offset(cx - rx, cy - ry),
                    size = Size(rx * 2, ry * 2)
                )
                // Sun dot position on arc
                val angleRad = ((180.0 - progress * 180.0) * PI / 180.0)
                val sunX = cx + rx * cos(angleRad).toFloat()
                val sunY = cy - ry * sin(angleRad).toFloat()

                // Sun glow
                drawCircle(
                    color = Color(0xFFFFE082),
                    radius = 14.dp.toPx(),
                    center = Offset(sunX, sunY)
                )
                // Sun core
                drawCircle(
                    color = Color(0xFFFFB300),
                    radius = 8.dp.toPx(),
                    center = Offset(sunX, sunY)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Sunrise", color = Color(0xFF828282), fontSize = 12.sp)
                Text(
                    text = sunrise,
                    color = Color(0xFF333333),
                    fontSize = 14.sp,
                    fontWeight = FontWeight(600)
                )
            }
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                Text(text = "Sunset", color = Color(0xFF828282), fontSize = 12.sp)
                Text(
                    text = sunset,
                    color = Color(0xFF333333),
                    fontSize = 14.sp,
                    fontWeight = FontWeight(600)
                )
            }
        }
    }
}

private fun parseTimeToHour(time: String): Float {
    return try {
        val parts = time.trim().split(" ")
        val timeParts = parts[0].split(":")
        var hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()
        val isPM = parts.getOrNull(1)?.uppercase() == "PM"
        if (isPM && hour != 12) hour += 12
        if (!isPM && hour == 12) hour = 0
        hour + minute / 60f
    } catch (e: Exception) {
        0f
    }
}

@Composable
fun DetailHourWeatherList(
    state: LazyListState,
    forecastDay: ForecastDay,
    hourNow: Int,
    timezoneId: String = "",
    onClickChooseHour: (HourWeather) -> Unit
) {
    val hours = forecastDay.hours
    LazyRow(
        state = state,
        contentPadding = PaddingValues(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(hours.size) { index ->
            DetailHourItem(
                modifier = Modifier.clickable { onClickChooseHour(hours[index]) },
                hour = hours[index],
                isCurrentHour = index == hourNow,
                timezoneId = timezoneId
            )
        }
    }
}

@Composable
fun DetailHourItem(
    modifier: Modifier = Modifier,
    hour: HourWeather,
    isCurrentHour: Boolean,
    timezoneId: String = ""
) {
    val bgColor = if (isCurrentHour) Color.White.copy(alpha = 0.3f) else Color.Transparent
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isCurrentHour) {
            Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color.White))
            Spacer(modifier = Modifier.height(2.dp))
        } else {
            Spacer(modifier = Modifier.height(7.dp))
        }
        Text(
            text = "${convertEpochToHour(hour.timeEpoch, timezoneId)}:00",
            color = Color.White,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        WeatherIcon(iconUrl = hour.condition?.icon ?: "", modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${hour.tempC.roundToInt()}°",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
