package org.psyche.assistant.Helper

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateFormatHelper {
     fun formatDateTime(isoDateTimeString: String): String {
        // Parse the ISO 8601 date-time string
        val instant = Instant.parse(isoDateTimeString)

        // Convert the Instant to LocalDateTime in the system's time zone
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        // Format the LocalDateTime to the desired string format
        val day = dateTime.date.dayOfMonth.toString().padStart(2, '0')
        val month = dateTime.date.monthNumber.toString().padStart(2, '0')
        val year = dateTime.date.year.toString()
        val hour = dateTime.hour.toString().padStart(2, '0')
        val minute = dateTime.minute.toString().padStart(2, '0')

        return "$day/$month/$year $hour:$minute"
    }
}