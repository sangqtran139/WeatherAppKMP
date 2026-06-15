package com.sangtq.weatherappkmp.ui.components

import androidx.compose.ui.graphics.Color

fun weatherBackgroundColors(hour: Int): List<Color> = when (hour) {
    in 5..16 -> listOf(Color(0xFF2353C7), Color(0xFF4884DA), Color(0xFFADC0CF), Color(0xFFF6DAB8))
    in 16..17 -> listOf(Color(0xFF3143A5), Color(0xFF6D6DC7), Color(0xFFC9A2AE), Color(0xFFEFB8A3))
    in 18..19 -> listOf(Color(0xFF242E6F), Color(0xFF55519F), Color(0xFF7F63A7), Color(0xFFC781B6))
    else -> listOf(Color(0xFF15215A), Color(0xFF27317C), Color(0xFF363E98), Color(0xFF454CB5))
}
