package com.example.weatherinstabug.utils

fun getWindDirectionText(degrees: Double?): String {
    if (degrees == null) return ""

    val normalizedDegrees = ((degrees % 360) + 360) % 360

    return when {
        normalizedDegrees >= 348.75 || normalizedDegrees < 11.25 -> "N"
        normalizedDegrees < 33.75 -> "NNE"
        normalizedDegrees < 56.25 -> "NE"
        normalizedDegrees < 78.75 -> "ENE"
        normalizedDegrees < 101.25 -> "E"
        normalizedDegrees < 123.75 -> "ESE"
        normalizedDegrees < 146.25 -> "SE"
        normalizedDegrees < 168.75 -> "SSE"
        normalizedDegrees < 191.25 -> "S"
        normalizedDegrees < 213.75 -> "SSW"
        normalizedDegrees < 236.25 -> "SW"
        normalizedDegrees < 258.75 -> "WSW"
        normalizedDegrees < 281.25 -> "W"
        normalizedDegrees < 303.75 -> "WNW"
        normalizedDegrees < 326.25 -> "NW"
        normalizedDegrees < 348.75 -> "NNW"
        else -> ""
    }
}