package org.ronil.hissab.utils

import androidx.compose.runtime.compositionLocalOf
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json


val json = Json {
    ignoreUnknownKeys = true
}

fun getCurrentDate(): String {
    val currentMoment: Instant = Clock.System.now()
    val localDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

    val day = localDateTime.date.dayOfMonth.toString().padStart(2, '0') // Ensures two digits
    val month = localDateTime.date.monthNumber.toString().padStart(2, '0')
    val year = localDateTime.date.year

    return "$day-$month-$year" // Format as dd-MM-yyyy
}

fun getCurrentTime(): String {
    val currentMoment: Instant = Clock.System.now()
    val localDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

    val hour = localDateTime.time.hour % 12
    val formattedHour = if (hour == 0) 12 else hour // Handle midnight and noon
    val minute = localDateTime.time.minute.toString().padStart(2, '0')
    val amPm = if (localDateTime.time.hour < 12) "AM" else "PM"

    return "$formattedHour:$minute $amPm" // Format as hh:mm a
}

val LocalPreferenceManager =
    compositionLocalOf<PreferenceManager> { error("Preference Manager Not Provided") }

val LocalSnackBarProvider =
    compositionLocalOf< ShackBarState> { error("ShackBarState  Not Provided") }
