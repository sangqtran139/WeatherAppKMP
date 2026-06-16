package com.sangtq.weatherappkmp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangtq.weatherappkmp.domain.model.WeatherAlert
import org.jetbrains.compose.resources.stringResource
import weatherappkmp.shared.generated.resources.Res
import weatherappkmp.shared.generated.resources.action_dismiss
import weatherappkmp.shared.generated.resources.alert_areas
import weatherappkmp.shared.generated.resources.alert_default_event
import weatherappkmp.shared.generated.resources.alert_effective
import weatherappkmp.shared.generated.resources.alert_expires
import weatherappkmp.shared.generated.resources.alert_instructions
import weatherappkmp.shared.generated.resources.alert_more_count
import weatherappkmp.shared.generated.resources.alert_severity
import weatherappkmp.shared.generated.resources.alert_tap_to_read

@Composable
fun WeatherAlertBanner(alerts: List<WeatherAlert>, modifier: Modifier = Modifier) {
    if (alerts.isEmpty()) return
    var visible by remember { mutableStateOf(true) }
    var selected by remember { mutableStateOf<WeatherAlert?>(null) }
    if (!visible) return

    val first = alerts.first()
    val tint = severityColor(first.severity)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(tint.copy(alpha = 0.15f))
                .clickable { selected = first }
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                val defaultEvent = stringResource(Res.string.alert_default_event)
                val defaultHeadline = stringResource(Res.string.alert_tap_to_read)
                Text(
                    text = first.event.ifBlank { defaultEvent },
                    color = tint,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(700),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = first.headline.ifBlank { defaultHeadline },
                    color = Color(0xFF333333),
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (alerts.size > 1) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(
                            Res.string.alert_more_count,
                            alerts.size - 1,
                            if (alerts.size > 2) "s" else ""
                        ),
                        color = Color(0xFF828282),
                        fontSize = 11.sp
                    )
                }
            }
            Box(modifier = Modifier.size(28.dp).clip(RoundedCornerShape(14.dp)).clickable { visible = false }, contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Close, contentDescription = stringResource(Res.string.action_dismiss), tint = Color(0xFF828282), modifier = Modifier.size(18.dp))
            }
        }
    }

    selected?.let { alert ->
        AlertDetailSheet(alert = alert, onDismiss = { selected = null })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlertDetailSheet(alert: WeatherAlert, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val tint = severityColor(alert.severity)
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val defaultEvent = stringResource(Res.string.alert_default_event)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, null, tint = tint, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(alert.event.ifBlank { defaultEvent }, color = tint, fontSize = 18.sp, fontWeight = FontWeight(700))
            }
            if (alert.headline.isNotBlank()) Text(alert.headline, color = Color(0xFF333333), fontSize = 14.sp, fontWeight = FontWeight(600))
            if (alert.severity.isNotBlank()) Text(stringResource(Res.string.alert_severity, alert.severity), color = Color(0xFF828282), fontSize = 12.sp)
            if (alert.areas.isNotBlank()) Text(stringResource(Res.string.alert_areas, alert.areas), color = Color(0xFF828282), fontSize = 12.sp)
            if (alert.effective.isNotBlank()) Text(stringResource(Res.string.alert_effective, alert.effective), color = Color(0xFF828282), fontSize = 12.sp)
            if (alert.expires.isNotBlank()) Text(stringResource(Res.string.alert_expires, alert.expires), color = Color(0xFF828282), fontSize = 12.sp)
            if (alert.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(alert.description, color = Color(0xFF333333), fontSize = 13.sp)
            }
            if (alert.instruction.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(stringResource(Res.string.alert_instructions), color = Color(0xFF333333), fontSize = 13.sp, fontWeight = FontWeight(700))
                Text(alert.instruction, color = Color(0xFF333333), fontSize = 13.sp)
            }
        }
    }
}

private fun severityColor(severity: String): Color = when (severity.lowercase()) {
    "extreme" -> Color(0xFFB71C1C)
    "severe" -> Color(0xFFEB5757)
    "moderate" -> Color(0xFFF2994A)
    "minor" -> Color(0xFFF2C94C)
    else -> Color(0xFFEB5757)
}
