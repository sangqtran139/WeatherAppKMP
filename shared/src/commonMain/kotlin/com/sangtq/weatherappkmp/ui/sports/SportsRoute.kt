package com.sangtq.weatherappkmp.ui.sports

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GolfCourse
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangtq.weatherappkmp.domain.model.SportCategory
import com.sangtq.weatherappkmp.domain.model.SportEvent
import com.sangtq.weatherappkmp.domain.model.SportsData
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import com.sangtq.weatherappkmp.ui.components.WeatherErrorScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SportsRoute(
    onBackClick: () -> Unit,
    viewModel: SportsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val selected by viewModel.selectedCategory.collectAsStateWithLifecycle()

    SportsScreen(
        state = state,
        selected = selected,
        onBackClick = onBackClick,
        onSelectCategory = viewModel::selectCategory,
        onRetry = viewModel::load
    )
}

@Composable
fun SportsScreen(
    state: Resource<SportsData>,
    selected: SportCategory,
    onBackClick: () -> Unit,
    onSelectCategory: (SportCategory) -> Unit,
    onRetry: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF2F2F2))) {
        TopBar(onBackClick = onBackClick)
        Spacer(modifier = Modifier.height(8.dp))
        CategoryTabs(selected = selected, onSelect = onSelectCategory)
        Spacer(modifier = Modifier.height(16.dp))

        when (val s = state) {
            Resource.Loading -> LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                color = Color(0xFFEB5757)
            )
            is Resource.Error -> WeatherErrorScreen(message = s.message, onRetry = { onRetry() })
            is Resource.Success -> {
                val events = when (selected) {
                    SportCategory.FOOTBALL -> s.data.football
                    SportCategory.CRICKET -> s.data.cricket
                    SportCategory.GOLF -> s.data.golf
                }
                EventList(events = events)
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
            modifier = Modifier.clip(CircleShape).background(Color.White).clickable(onClick = onBackClick).padding(10.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color(0xFF2F80ED), modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text("Sports events", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight(600))
    }
}

@Composable
private fun CategoryTabs(selected: SportCategory, onSelect: (SportCategory) -> Unit) {
    val items = listOf(
        Triple(SportCategory.FOOTBALL, Icons.Default.SportsSoccer, "Football"),
        Triple(SportCategory.CRICKET, Icons.Default.SportsCricket, "Cricket"),
        Triple(SportCategory.GOLF, Icons.Default.GolfCourse, "Golf")
    )
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { (cat, icon, label) ->
            CategoryChip(label = label, icon = icon, isSelected = cat == selected, onClick = { onSelect(cat) })
        }
    }
}

@Composable
private fun CategoryChip(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    val bg = if (isSelected) Color(0xFFEB5757) else Color.White
    val fg = if (isSelected) Color.White else Color(0xFF333333)
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = fg, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, color = fg, fontSize = 13.sp, fontWeight = FontWeight(600))
    }
}

@Composable
private fun EventList(events: List<SportEvent>) {
    if (events.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(top = 40.dp), contentAlignment = Alignment.TopCenter) {
            Text("No events available", color = Color(0xFF828282), fontSize = 14.sp)
        }
        return
    }
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(events) { event -> EventCard(event = event) }
    }
}

@Composable
private fun EventCard(event: SportEvent) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(event.match.ifBlank { event.tournament }, color = Color(0xFF333333), fontSize = 15.sp, fontWeight = FontWeight(700))
        if (event.tournament.isNotBlank() && event.match.isNotBlank()) {
            Text(event.tournament, color = Color(0xFF828282), fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (event.stadium.isNotBlank()) {
            InfoRow(Icons.Default.LocationOn, "${event.stadium}${if (event.country.isNotBlank()) ", ${event.country}" else ""}")
        }
        if (event.start.isNotBlank()) {
            InfoRow(Icons.Default.Schedule, event.start)
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
        Icon(icon, null, tint = Color(0xFFEB5757), modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, color = Color(0xFF333333), fontSize = 13.sp)
    }
}
