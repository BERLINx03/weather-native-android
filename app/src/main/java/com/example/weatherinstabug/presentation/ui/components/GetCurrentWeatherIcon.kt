package com.example.weatherinstabug.presentation.ui.components

import com.example.weatherinstabug.R


fun getCurrentWeatherIcon(icon: String): Int {
    return when (icon) {
        "cloudy" -> R.drawable.cloudy
        "clear", "clear-night", "clear-day" -> R.drawable.partly_cloudy
        "partly-cloudy-day", "partly-cloudy-night" -> R.drawable.cloudy_sunny
        "rain", "rain-showers", "rain-showers-day", "rain-showers-night" -> R.drawable.rainy
        "fog", "fog-showers", "fog-showers-day", "fog-showers-night" -> R.drawable.fog
        else -> R.drawable.cloudy_sunny
    }
}