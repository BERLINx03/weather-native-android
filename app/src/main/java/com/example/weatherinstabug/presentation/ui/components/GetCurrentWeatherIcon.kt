package com.example.weatherinstabug.presentation.ui.components

import com.example.weatherinstabug.R


fun getCurrentWeatherIcon(icon: String): Int {
    return when (icon) {
        "cloudy" -> R.drawable.cloudy
        "clear", "clear-night", "clear-day" -> R.drawable.cloudy_sunny
        else -> R.drawable.cloudy_sunny
    }
}