package com.sangtq.weatherappkmp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangtq.weatherappkmp.model.NoteDetailWeather
import com.sangtq.weatherappkmp.util.convertToCamelCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherInfoBottomSheet(noteType: String, onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        containerColor = Color.White,
    ) {
        Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 32.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = when (noteType) {
                        NoteDetailWeather.PRECIPITATION.name -> Icons.Default.WaterDrop
                        NoteDetailWeather.WINDY.name -> Icons.Default.Air
                        NoteDetailWeather.HUMIDITY.name -> Icons.Default.Opacity
                        else -> Icons.Default.WbSunny
                    },
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (noteType == NoteDetailWeather.INDEX_UV.name) "INDEX UV"
                    else convertToCamelCase(noteType),
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = when (noteType) {
                    NoteDetailWeather.PRECIPITATION.name ->
                        "Precipitation is the fall of water in either liquid or frozen form from the atmosphere to the earth's surface"
                    NoteDetailWeather.WINDY.name ->
                        "Wind speed is a unit that measures the speed of air flow from high pressure to low pressure."
                    NoteDetailWeather.HUMIDITY.name ->
                        "Humidity is the level of air wetness (the amount of water contained in the air) expressed as a percentage relative to the saturation point."
                    else ->
                        "The UV index is a number without units to explain the level of exposure to ultraviolet radiation that is related to human health.\n\nUV Index 0-2 = Low\nUV Index 3-5 = Moderate\nUV Index 6-7 = High\nUV Index 8-10 = Very High\nUV Index >11 = Extreme"
                },
                fontSize = 14.sp,
                color = Color(0xFF828282)
            )
        }
    }
}
