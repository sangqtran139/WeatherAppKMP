package com.sangtq.weatherappkmp.ui.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangtq.weatherappkmp.ui.components.DatedWeatherScreen
import com.sangtq.weatherappkmp.util.todayIsoDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HistoryRoute(
    location: String,
    onBackClick: () -> Unit,
    viewModel: HistoryViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val date by viewModel.selectedDate.collectAsStateWithLifecycle()

    LaunchedEffect(location) { viewModel.load(location) }

    val today = todayIsoDate()
    DatedWeatherScreen(
        title = "Historical weather",
        state = state,
        date = date,
        gradient = listOf(Color(0xFF5D4037), Color(0xFF6D4C41), Color(0xFF795548)),
        onBackClick = onBackClick,
        onPreviousDay = { viewModel.shiftDays(-1) },
        onNextDay = { viewModel.shiftDays(1) },
        onRetry = { viewModel.load(location) },
        canGoPrev = true,
        canGoNext = date < today
    )
}
