package com.example.weatherinstabug.domain.repository

import com.example.weatherinstabug.data.WeatherResponse

interface WeatherDataParser {
    fun parseWeatherData(jsonData: String): WeatherResponse
}