package com.sangtq.weatherappkmp.util

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
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

private fun LocalDate.toIso(): String {
    val mm = (month.ordinal + 1).toString().padStart(2, '0')
    val dd = dayOfMonth.toString().padStart(2, '0')
    return "$year-$mm-$dd"
}

private fun parseIso(isoDate: String): LocalDate? {
    val parts = isoDate.split("-")
    if (parts.size != 3) return null
    return runCatching { LocalDate(parts[0].toInt(), parts[1].toInt(), parts[2].toInt()) }.getOrNull()
}

/** Returns today as "yyyy-MM-dd" in system local timezone (the format WeatherAPI expects). */
fun todayIsoDate(): String {
    val instant = Instant.fromEpochMilliseconds(kotlin.time.Clock.System.now().toEpochMilliseconds())
    val ldt = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return ldt.date.toIso()
}

/** Add `days` to an ISO date string (yyyy-MM-dd). Negative = past. */
fun addDaysToIsoDate(isoDate: String, days: Int): String {
    val date = parseIso(isoDate) ?: return isoDate
    return date.plus(DatePeriod(days = days)).toIso()
}

/** Human format an ISO date string. */
fun humanReadableIsoDate(isoDate: String): String {
    val date = parseIso(isoDate) ?: return isoDate
    val day = date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
    val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }
    return "$day, ${date.dayOfMonth} $month ${date.year}"
}
