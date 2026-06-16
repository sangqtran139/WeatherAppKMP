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
import androidx.compose.material.icons.filled.Language
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
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import weatherappkmp.shared.generated.resources.Res
import weatherappkmp.shared.generated.resources.feature_astronomy_desc
import weatherappkmp.shared.generated.resources.feature_astronomy_title
import weatherappkmp.shared.generated.resources.feature_future_desc
import weatherappkmp.shared.generated.resources.feature_future_title
import weatherappkmp.shared.generated.resources.feature_history_desc
import weatherappkmp.shared.generated.resources.feature_history_title
import weatherappkmp.shared.generated.resources.feature_language_desc
import weatherappkmp.shared.generated.resources.feature_language_title
import weatherappkmp.shared.generated.resources.feature_marine_desc
import weatherappkmp.shared.generated.resources.feature_marine_title
import weatherappkmp.shared.generated.resources.feature_menu_title
import weatherappkmp.shared.generated.resources.feature_sports_desc
import weatherappkmp.shared.generated.resources.feature_sports_title

private sealed class FeatureMenuItem(
    val titleRes: StringResource,
    val descriptionRes: StringResource,
    val icon: ImageVector,
    val tint: Color
) {
    class Navigate(
        titleRes: StringResource,
        descriptionRes: StringResource,
        icon: ImageVector,
        tint: Color,
        val screen: Screen
    ) : FeatureMenuItem(titleRes, descriptionRes, icon, tint)

    class Language(
        titleRes: StringResource,
        descriptionRes: StringResource,
        icon: ImageVector,
        tint: Color
    ) : FeatureMenuItem(titleRes, descriptionRes, icon, tint)
}

private val featureMenuItems: List<FeatureMenuItem> = listOf(
    FeatureMenuItem.Navigate(
        titleRes = Res.string.feature_astronomy_title,
        descriptionRes = Res.string.feature_astronomy_desc,
        icon = Icons.Default.NightsStay,
        tint = Color(0xFF6C5CE7),
        screen = Screen.Astronomy
    ),
    FeatureMenuItem.Navigate(
        titleRes = Res.string.feature_marine_title,
        descriptionRes = Res.string.feature_marine_desc,
        icon = Icons.Default.DirectionsBoat,
        tint = Color(0xFF00B4D8),
        screen = Screen.Marine
    ),
    FeatureMenuItem.Navigate(
        titleRes = Res.string.feature_sports_title,
        descriptionRes = Res.string.feature_sports_desc,
        icon = Icons.Default.SportsSoccer,
        tint = Color(0xFFEB5757),
        screen = Screen.Sports
    ),
    FeatureMenuItem.Navigate(
        titleRes = Res.string.feature_history_title,
        descriptionRes = Res.string.feature_history_desc,
        icon = Icons.Default.History,
        tint = Color(0xFFF2994A),
        screen = Screen.History
    ),
    FeatureMenuItem.Navigate(
        titleRes = Res.string.feature_future_title,
        descriptionRes = Res.string.feature_future_desc,
        icon = Icons.Default.CalendarMonth,
        tint = Color(0xFF27AE60),
        screen = Screen.Future
    ),
    FeatureMenuItem.Language(
        titleRes = Res.string.feature_language_title,
        descriptionRes = Res.string.feature_language_desc,
        icon = Icons.Default.Language,
        tint = Color(0xFF2F80ED)
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureMenuBottomSheet(
    onDismiss: () -> Unit,
    onSelect: (Screen) -> Unit,
    onSelectLanguage: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            Text(
                text = stringResource(Res.string.feature_menu_title),
                color = Color(0xFF333333),
                fontSize = 18.sp,
                fontWeight = FontWeight(700),
                modifier = Modifier.padding(start = 24.dp, top = 4.dp, bottom = 12.dp)
            )
            LazyColumn(contentPadding = PaddingValues(horizontal = 12.dp)) {
                items(featureMenuItems) { item ->
                    FeatureRow(item = item, onClick = {
                        when (item) {
                            is FeatureMenuItem.Navigate -> onSelect(item.screen)
                            is FeatureMenuItem.Language -> onSelectLanguage()
                        }
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
            Text(stringResource(item.titleRes), color = Color(0xFF333333), fontSize = 15.sp, fontWeight = FontWeight(600))
            Text(stringResource(item.descriptionRes), color = Color(0xFF828282), fontSize = 12.sp)
        }
    }
}
