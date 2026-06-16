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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangtq.weatherappkmp.domain.model.SportCategory
import com.sangtq.weatherappkmp.domain.model.SportEvent
import com.sangtq.weatherappkmp.domain.model.SportsData
import com.sangtq.weatherappkmp.model.basenetwork.Resource
import com.sangtq.weatherappkmp.ui.components.WeatherErrorScreen
import org.koin.compose.viewmodel.koinViewModel

private val popularCities = listOf("London", "New York", "Sydney", "Tokyo", "Mumbai")

@Composable
fun SportsRoute(
    location: String,
    onBackClick: () -> Unit,
    viewModel: SportsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val selected by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val activeQuery by viewModel.query.collectAsStateWithLifecycle()

    LaunchedEffect(location) { viewModel.load(location) }

    SportsScreen(
        state = state,
        selected = selected,
        activeQuery = activeQuery,
        onBackClick = onBackClick,
        onSelectCategory = viewModel::selectCategory,
        onRetry = { viewModel.load(activeQuery.ifBlank { location }) },
        onTryCity = { viewModel.load(it) }
    )
}

@Composable
fun SportsScreen(
    state: Resource<SportsData>,
    selected: SportCategory,
    activeQuery: String,
    onBackClick: () -> Unit,
    onSelectCategory: (SportCategory) -> Unit,
    onRetry: () -> Unit,
    onTryCity: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF2F2F2))) {
        TopBar(onBackClick = onBackClick, activeQuery = activeQuery)
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
                EventList(
                    events = events,
                    activeQuery = activeQuery,
                    onTryCity = onTryCity
                )
            }
        }
    }
}

@Composable
private fun TopBar(onBackClick: () -> Unit, activeQuery: String) {
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
        Column {
            Text("Sports events", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight(600))
            if (activeQuery.isNotBlank()) {
                Text("Near $activeQuery", color = Color(0xFF828282), fontSize = 12.sp)
            }
        }
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
private fun EventList(events: List<SportEvent>, activeQuery: String, onTryCity: (String) -> Unit) {
    if (events.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No events found near \"$activeQuery\"",
                color = Color(0xFF333333),
                fontSize = 15.sp,
                fontWeight = FontWeight(600),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "WeatherAPI only returns events within the area. Try a major city below.",
                color = Color(0xFF828282),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(popularCities) { city ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White)
                            .clickable { onTryCity(city) }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(city, color = Color(0xFFEB5757), fontSize = 13.sp, fontWeight = FontWeight(600))
                    }
                }
            }
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
