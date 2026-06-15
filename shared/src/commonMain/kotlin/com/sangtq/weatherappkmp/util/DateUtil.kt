package com.sangtq.weatherappkmp.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun convertEpochToLocalDate(epoch: Long): String {
    val instant = Instant.fromEpochSeconds(epoch)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val day = localDateTime.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
    val month = localDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
    return "$day, ${localDateTime.dayOfMonth} $month"
}

fun convertEpochToHour(epoch: Long): Int {
    val instant = Instant.fromEpochSeconds(epoch)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.hour
}

fun getCurrentTime(): String {
    val epochMs = kotlin.time.Clock.System.now().toEpochMilliseconds()
    val instant = Instant.fromEpochMilliseconds(epochMs)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = localDateTime.hour.toString().padStart(2, '0')
    val minute = localDateTime.minute.toString().padStart(2, '0')
    return "$hour:$minute"
}

fun convertToCamelCase(input: String): String {
    return input.lowercase().split("_").joinToString("") { word ->
        word.replaceFirstChar { it.uppercase() }
    }
}
