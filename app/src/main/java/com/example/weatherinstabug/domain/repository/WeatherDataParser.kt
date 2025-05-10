package com.example.weatherinstabug.domain.repository

import com.example.weatherinstabug.data.WeatherResponseDto

interface WeatherDataParser {
    fun parseWeatherData(jsonData: String): WeatherResponseDto
}