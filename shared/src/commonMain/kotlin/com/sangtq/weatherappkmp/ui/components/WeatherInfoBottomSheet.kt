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
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import weatherappkmp.shared.generated.resources.Res
import weatherappkmp.shared.generated.resources.info_humidity_desc
import weatherappkmp.shared.generated.resources.info_humidity_title
import weatherappkmp.shared.generated.resources.info_precipitation_desc
import weatherappkmp.shared.generated.resources.info_precipitation_title
import weatherappkmp.shared.generated.resources.info_uv_desc
import weatherappkmp.shared.generated.resources.info_wind_desc
import weatherappkmp.shared.generated.resources.info_wind_title
import weatherappkmp.shared.generated.resources.metric_index_uv_caps

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherInfoBottomSheet(noteType: String, onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        containerColor = Color.White,
    ) {
        val titleRes: StringResource = when (noteType) {
            NoteDetailWeather.PRECIPITATION.name -> Res.string.info_precipitation_title
            NoteDetailWeather.WINDY.name -> Res.string.info_wind_title
            NoteDetailWeather.HUMIDITY.name -> Res.string.info_humidity_title
            else -> Res.string.metric_index_uv_caps
        }
        val descRes: StringResource = when (noteType) {
            NoteDetailWeather.PRECIPITATION.name -> Res.string.info_precipitation_desc
            NoteDetailWeather.WINDY.name -> Res.string.info_wind_desc
            NoteDetailWeather.HUMIDITY.name -> Res.string.info_humidity_desc
            else -> Res.string.info_uv_desc
        }
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
                    text = stringResource(titleRes),
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(descRes),
                fontSize = 14.sp,
                color = Color(0xFF828282)
            )
        }
    }
}
