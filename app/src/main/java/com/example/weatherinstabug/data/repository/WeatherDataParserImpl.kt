package com.example.weatherinstabug.data.repository

import com.example.weatherinstabug.data.WeatherResponseDto
import com.example.weatherinstabug.domain.repository.WeatherDataParser
import com.example.weatherinstabug.utils.parseResponseIntoWeather

class WeatherDataParserImpl : WeatherDataParser {
    override fun parseWeatherData(jsonData: String): WeatherResponseDto {
        return parseResponseIntoWeather(jsonData)
    }
}