package com.sangtq.weatherappkmp.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangtq.weatherappkmp.Screen
import com.sangtq.weatherappkmp.domain.model.ForecastDay
import com.sangtq.weatherappkmp.domain.model.HourWeather
import com.sangtq.weatherappkmp.domain.model.WeatherData
import com.sangtq.weatherappkmp.model.NoteDetailWeather
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import com.sangtq.weatherappkmp.ui.components.AnimatedShimmer
import com.sangtq.weatherappkmp.ui.components.FeatureMenuBottomSheet
import com.sangtq.weatherappkmp.ui.components.WeatherAlertBanner
import com.sangtq.weatherappkmp.ui.components.WeatherErrorScreen
import com.sangtq.weatherappkmp.ui.components.WeatherIcon
import com.sangtq.weatherappkmp.ui.components.WeatherInfoBottomSheet
import com.sangtq.weatherappkmp.ui.components.weatherBackgroundColors
import com.sangtq.weatherappkmp.ui.permission.RequestLocationPermission
import com.sangtq.weatherappkmp.util.convertEpochToHour
import com.sangtq.weatherappkmp.util.convertEpochToLocalDate
import com.sangtq.weatherappkmp.util.getCurrentTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun WeatherHomeRoute(
    modifier: Modifier = Modifier,
    location: String = "Vietnam",
    permissionHandled: Boolean = false,
    onPermissionHandled: () -> Unit = {},
    onLocationDetected: (String) -> Unit = {},
    viewModel: WeatherHomeViewModel = koinViewModel(),
    openWeatherDetail: (() -> Unit)? = null,
    openSearch: (() -> Unit)? = null,
    openFeature: (Screen) -> Unit = {}
) {
    if (!permissionHandled) {
        RequestLocationPermission(
            onGranted = {
                onPermissionHandled()
                viewModel.detectAndLoadWeather(
                    fallback = location,
                    onLocationDetected = onLocationDetected
                )
            },
            onDenied = {
                onPermissionHandled()
                viewModel.loadWeather(location)
            }
        )
    }

    // Reload when location changes (e.g. user picked a city from search).
    LaunchedEffect(location) {
        if (permissionHandled) viewModel.loadWeather(location)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isCurrentFavorite.collectAsStateWithLifecycle()

    var weatherData by remember { mutableStateOf<WeatherData?>(null) }

    when (val state = uiState) {
        Resource.Loading -> {
            AnimatedShimmer(); return
        }

        is Resource.Error -> {
            WeatherErrorScreen(message = state.message, onRetry = viewModel::loadWeather); return
        }

        is Resource.Success -> weatherData = state.data
    }

    val data = weatherData ?: return

    val listHourState = rememberLazyListState()
    val hourNow = remember { mutableIntStateOf(0) }
    val backgroundColors = weatherBackgroundColors(hourNow.intValue)
    var showBottomSheet by remember { mutableStateOf(false) }
    var noteTypeForBottomSheet by remember { mutableStateOf("") }
    var showFeatureMenu by remember { mutableStateOf(false) }
    val currentTime = remember { mutableStateOf(getCurrentTime()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime.value = getCurrentTime()
            delay(10000)
        }
    }

    LaunchedEffect(data) {
        hourNow.intValue =
            convertEpochToHour(data.location.localtimeEpoch, data.location.timezoneId)
        listHourState.animateScrollToItem(
            if (hourNow.intValue > 6) hourNow.intValue - 2 else hourNow.intValue
        )
    }

    Column(
        modifier = modifier.fillMaxSize().background(Color(0xFFF2F2F2)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            contentPadding = PaddingValues(top = 36.dp, start = 12.dp, end = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White)
                            .clickable { openSearch?.invoke() }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            null,
                            tint = Color(0xFF2F80ED),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${data.location.name}, ${data.location.country}",
                            color = Color.Black,
                            fontSize = 15.sp,
                            fontWeight = FontWeight(500),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable { viewModel.toggleFavorite() }
                                .padding(10.dp)
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = if (isFavorite) "Remove favorite" else "Add favorite",
                                tint = if (isFavorite) Color(0xFFF2C94C) else Color(0xFF2F80ED),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable { showFeatureMenu = true }
                                .padding(10.dp)
                        ) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "More features",
                                tint = Color(0xFF2F80ED),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
                if (data.alerts.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    WeatherAlertBanner(alerts = data.alerts)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .background(Brush.verticalGradient(colors = backgroundColors))
                        .padding(start = 24.dp, bottom = 44.dp)
                ) {
                    Spacer(modifier = Modifier.height(36.dp))
                    Text(
                        text = "Today",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight(500)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = convertEpochToLocalDate(data.location.localtimeEpoch) + " | ${currentTime.value}",
                        color = Color.White, fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    DetailWeatherToday(hourNow = hourNow.intValue, weatherData = data)
                }
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                        .background(Color.White)
                ) {
                    val firstDay = data.forecastDays.firstOrNull() ?: return@item
                    if (firstDay.hours.isEmpty()) return@item

                    HourWeatherLazyList(
                        modifier = Modifier,
                        contentPaddingValues = PaddingValues(
                            start = 12.dp,
                            top = 8.dp,
                            bottom = 14.dp,
                            end = 12.dp
                        ),
                        spaceBy = Arrangement.spacedBy(8.dp),
                        forecastDay = firstDay,
                        timezoneId = data.location.timezoneId,
                        state = listHourState,
                        hourNow = hourNow.intValue,
                        onClickChooseHour = {
                            hourNow.intValue =
                                convertEpochToHour(it.timeEpoch, data.location.timezoneId)
                        }
                    )
                    HorizontalDivider(thickness = 1.dp, color = Color(0xFFE0E0E0))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .clickable { openWeatherDetail?.invoke() },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 18.dp),
                            text = "Read more", fontSize = 14.sp,
                            color = Color(0xFF2F80ED), fontWeight = FontWeight(500)
                        )
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward, null,
                            tint = Color(0xFF2F80ED),
                            modifier = Modifier.padding(start = 4.dp).size(16.dp)
                        )
                    }
                }
            }
            forecastWeatherLazyList(forecastDays = data.forecastDays) { noteType ->
                noteTypeForBottomSheet = noteType
                showBottomSheet = true
            }
        }
    }

    if (showBottomSheet) {
        WeatherInfoBottomSheet(
            noteType = noteTypeForBottomSheet,
            onDismiss = { showBottomSheet = false })
    }

    if (showFeatureMenu) {
        FeatureMenuBottomSheet(
            onDismiss = { showFeatureMenu = false },
            onSelect = openFeature
        )
    }
}

@Composable
fun DetailWeatherToday(modifier: Modifier = Modifier, hourNow: Int, weatherData: WeatherData) {
    Row(modifier = modifier) {
        Column(modifier = Modifier.weight(0.6f)) {
            Text(
                text = "${weatherData.forecastDays.firstOrNull()?.hours?.getOrNull(hourNow)?.tempC?.roundToInt()}°",
                color = Color.White, fontSize = 90.sp, fontWeight = FontWeight(500)
            )
            Text(
                text = "Afternoon ${weatherData.forecastDays.firstOrNull()?.day?.maxTempC?.roundToInt()}°C, " +
                        "Night ${weatherData.forecastDays.firstOrNull()?.day?.minTempC?.roundToInt()}°C",
                color = Color.White, fontSize = 14.sp
            )
        }
        Column(
            modifier = Modifier.weight(0.4f).padding(top = 12.dp, end = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            WeatherIcon(
                iconUrl = weatherData.forecastDays.firstOrNull()?.hours?.getOrNull(hourNow)?.condition?.icon
                    ?: "",
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = weatherData.forecastDays.firstOrNull()?.hours?.getOrNull(hourNow)?.condition?.text
                    ?: "",
                color = Color.White, fontSize = 14.sp,
                textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun HourWeatherLazyList(
    modifier: Modifier = Modifier,
    state: LazyListState,
    forecastDay: ForecastDay,
    contentPaddingValues: PaddingValues,
    spaceBy: Arrangement.Horizontal,
    hourNow: Int,
    timezoneId: String = "",
    onClickChooseHour: (HourWeather) -> Unit
) {
    LazyRow(
        state = state, modifier = modifier,
        contentPadding = contentPaddingValues,
        horizontalArrangement = spaceBy
    ) {
        items(forecastDay.hours.size) { index ->
            HourlyWeatherItem(
                modifier = Modifier.clickable { onClickChooseHour(forecastDay.hours[index]) },
                hour = forecastDay.hours[index],
                timezoneId = timezoneId,
                isCurrentHour = index == hourNow
            )
        }
    }
}

@Composable
fun HourlyWeatherItem(
    modifier: Modifier = Modifier,
    hour: HourWeather,
    isCurrentHour: Boolean,
    timezoneId: String = ""
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (isCurrentHour) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFEB5757)))
        } else {
            Spacer(modifier = Modifier.height(6.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${convertEpochToHour(hour.timeEpoch, timezoneId)}:00",
            color = Color.Black,
            fontSize = 12.sp,
            fontWeight = FontWeight(450)
        )
        Spacer(modifier = Modifier.height(4.dp))
        WeatherIcon(
            iconUrl = hour.condition.icon,
            modifier = Modifier.padding(horizontal = 13.dp).size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${hour.tempC.roundToInt()}°C",
            color = Color.Black,
            fontSize = 12.sp,
            fontWeight = FontWeight(450)
        )
    }
}

fun LazyListScope.forecastWeatherLazyList(
    forecastDays: List<ForecastDay>,
    onClickTips: (String) -> Unit
) {
    if (forecastDays.isEmpty()) return
    item {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color.White).fillMaxWidth()
                .padding(top = 20.dp, start = 24.dp, end = 24.dp),
            text = "Weather for the next ${forecastDays.size} days",
            color = Color(0xFF333333), fontSize = 16.sp, fontWeight = FontWeight(600)
        )
    }
    items(forecastDays.size) { index ->
        WeatherForecastItem(
            modifier = Modifier.background(Color.White).animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ),
            forecastDay = forecastDays[index],
            onClickTips = onClickTips
        )
    }
    item {
        Text(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .background(Color.White).fillMaxWidth()
                .padding(top = 6.dp, bottom = 14.dp, start = 24.dp, end = 24.dp),
            text = "Developed by SangTran",
            color = Color(0xFF828282), fontSize = 10.sp, fontWeight = FontWeight(600)
        )
        Spacer(modifier = Modifier.height(70.dp))
    }
}

@Composable
fun WeatherForecastItem(
    modifier: Modifier = Modifier,
    forecastDay: ForecastDay,
    onClickTips: (String) -> Unit
) {
    val isExpanded = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val animateRotation = remember { Animatable(0f) }

    Column(modifier = modifier.clickable {
        isExpanded.value = !isExpanded.value
        coroutineScope.launch { animateRotation.animateTo(if (isExpanded.value) 180f else 0f) }
    }) {
        Spacer(modifier = Modifier.height(18.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Column(modifier = Modifier.weight(1f).padding(start = 24.dp, end = 10.dp)) {
                Text(
                    text = convertEpochToLocalDate(forecastDay.dateEpoch),
                    color = Color(0xFF333333), fontSize = 16.sp, fontWeight = FontWeight(450),
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Afternoon ${forecastDay.day.maxTempC.roundToInt()}° C, Night ${forecastDay.day.minTempC.roundToInt()}° C",
                    color = Color(0xFF828282), fontSize = 14.sp, fontWeight = FontWeight(450),
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
            WeatherIcon(iconUrl = forecastDay.day.condition.icon, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(20.dp))
            Icon(
                Icons.Default.KeyboardArrowDown, null,
                modifier = Modifier.clip(RoundedCornerShape(8.dp)).rotate(animateRotation.value)
                    .padding(6.dp)
            )
            Spacer(modifier = Modifier.width(18.dp))
        }
        if (isExpanded.value) {
            DetailWeatherForecastForDay(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 20.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .fillMaxWidth().padding(all = 16.dp),
                forecastDay = forecastDay,
                onClickTips = onClickTips
            )
        } else {
            Spacer(modifier = Modifier.height(12.dp))
        }
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFE0E0E0))
    }
}

@Composable
fun DetailWeatherForecastForDay(
    modifier: Modifier = Modifier,
    forecastDay: ForecastDay,
    onClickTips: (String) -> Unit
) {
    Column(modifier = modifier) {
        Row {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.WaterDrop,
                        null,
                        tint = Color(0xFF4884DA),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Precipitation",
                        color = Color(0xFF333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight(700)
                    )
                    Icon(
                        Icons.Default.Info,
                        null,
                        modifier = Modifier.clickable { onClickTips(NoteDetailWeather.PRECIPITATION.name) }
                            .padding(6.dp).size(16.dp)
                    )
                }
                Text(
                    "${forecastDay.day.dailyChanceOfRain}%",
                    color = Color(0xFF333333),
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.padding(start = 22.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Air, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Wind",
                        color = Color(0xFF333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight(700)
                    )
                    Icon(
                        Icons.Default.Info,
                        null,
                        modifier = Modifier.clickable { onClickTips(NoteDetailWeather.WINDY.name) }
                            .padding(6.dp).size(16.dp)
                    )
                }
                Text(
                    "${forecastDay.day.maxWindKph.roundToInt()} km/h",
                    color = Color(0xFF333333),
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.padding(start = 22.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Opacity, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Humidity",
                        color = Color(0xFF333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight(700)
                    )
                    Icon(
                        Icons.Default.Info,
                        null,
                        modifier = Modifier.clickable { onClickTips(NoteDetailWeather.HUMIDITY.name) }
                            .padding(6.dp).size(16.dp)
                    )
                }
                Text(
                    "${forecastDay.day.avgHumidity}%",
                    color = Color(0xFF333333),
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.padding(start = 22.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.WbSunny,
                        null,
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Index UV",
                        color = Color(0xFF333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight(700)
                    )
                    Icon(
                        Icons.Default.Info,
                        null,
                        modifier = Modifier.clickable { onClickTips(NoteDetailWeather.INDEX_UV.name) }
                            .padding(6.dp).size(16.dp)
                    )
                }
                Text(
                    "${forecastDay.day.uv}",
                    color = Color(0xFF333333),
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.padding(start = 22.dp)
                )
            }
        }
    }
}
