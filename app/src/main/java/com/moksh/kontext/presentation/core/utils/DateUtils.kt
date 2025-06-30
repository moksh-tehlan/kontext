package com.moksh.kontext.presentation.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    fun formatDateString(dateString: String): String {
        return try {
            // Try parsing ISO 8601 format with various patterns
            val inputPatterns = listOf(
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "yyyy-MM-dd'T'HH:mm:ss'Z'"
            )

            val outputFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())

            for (pattern in inputPatterns) {
                try {
                    val inputFormat = SimpleDateFormat(pattern, Locale.getDefault())
                    val date: Date = inputFormat.parse(dateString) ?: continue
                    return outputFormat.format(date)
                } catch (e: Exception) {
                    // Try next pattern
                    continue
                }
            }

            // If all patterns fail, return a fallback
            "Recent"
        } catch (e: Exception) {
            "Recent"
        }
    }
}