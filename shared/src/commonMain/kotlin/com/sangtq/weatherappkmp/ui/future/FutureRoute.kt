package com.sangtq.weatherappkmp.ui.future

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangtq.weatherappkmp.ui.components.DatedWeatherScreen
import com.sangtq.weatherappkmp.util.addDaysToIsoDate
import com.sangtq.weatherappkmp.util.todayIsoDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FutureRoute(
    location: String,
    onBackClick: () -> Unit,
    viewModel: FutureViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val date by viewModel.selectedDate.collectAsStateWithLifecycle()

    LaunchedEffect(location) { viewModel.load(location) }

    val today = todayIsoDate()
    val minDate = addDaysToIsoDate(today, 14)
    val maxDate = addDaysToIsoDate(today, 300)

    DatedWeatherScreen(
        title = "Future weather",
        state = state,
        date = date,
        gradient = listOf(Color(0xFF1B5E20), Color(0xFF2E7D32), Color(0xFF388E3C)),
        onBackClick = onBackClick,
        onPreviousDay = { viewModel.shiftDays(-1) },
        onNextDay = { viewModel.shiftDays(1) },
        onRetry = { viewModel.load(location) },
        canGoPrev = date > minDate,
        canGoNext = date < maxDate
    )
}
