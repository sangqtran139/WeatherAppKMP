package com.sangtq.weatherappkmp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangtq.weatherappkmp.Screen

private data class FeatureMenuItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val tint: Color,
    val screen: Screen
)

private val featureMenuItems = listOf(
    FeatureMenuItem(
        title = "Astronomy",
        description = "Sunrise, sunset, moon phase",
        icon = Icons.Default.NightsStay,
        tint = Color(0xFF6C5CE7),
        screen = Screen.Astronomy
    ),
    FeatureMenuItem(
        title = "Marine forecast",
        description = "Tides, waves, swell",
        icon = Icons.Default.DirectionsBoat,
        tint = Color(0xFF00B4D8),
        screen = Screen.Marine
    ),
    FeatureMenuItem(
        title = "Sports events",
        description = "Football, cricket, golf weather",
        icon = Icons.Default.SportsSoccer,
        tint = Color(0xFFEB5757),
        screen = Screen.Sports
    ),
    FeatureMenuItem(
        title = "Historical weather",
        description = "Look back at past weather",
        icon = Icons.Default.History,
        tint = Color(0xFFF2994A),
        screen = Screen.History
    ),
    FeatureMenuItem(
        title = "Future weather",
        description = "Forecast up to 300 days ahead",
        icon = Icons.Default.CalendarMonth,
        tint = Color(0xFF27AE60),
        screen = Screen.Future
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureMenuBottomSheet(
    onDismiss: () -> Unit,
    onSelect: (Screen) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            Text(
                text = "More features",
                color = Color(0xFF333333),
                fontSize = 18.sp,
                fontWeight = FontWeight(700),
                modifier = Modifier.padding(start = 24.dp, top = 4.dp, bottom = 12.dp)
            )
            LazyColumn(contentPadding = PaddingValues(horizontal = 12.dp)) {
                items(featureMenuItems) { item ->
                    FeatureRow(item = item, onClick = {
                        onSelect(item.screen)
                        onDismiss()
                    })
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
private fun FeatureRow(item: FeatureMenuItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(44.dp).clip(CircleShape).background(item.tint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(item.icon, contentDescription = null, tint = item.tint, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.padding(end = 8.dp), verticalArrangement = Arrangement.Center) {
            Text(item.title, color = Color(0xFF333333), fontSize = 15.sp, fontWeight = FontWeight(600))
            Text(item.description, color = Color(0xFF828282), fontSize = 12.sp)
        }
    }
}
