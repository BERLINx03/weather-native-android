package com.example.weatherinstabug.data.repository

import com.example.weatherinstabug.data.WeatherResponse
import com.example.weatherinstabug.domain.repository.WeatherDataParser
import com.example.weatherinstabug.utils.parseResponseIntoWeather

class WeatherDataParserImpl : WeatherDataParser {
    override fun parseWeatherData(jsonData: String): WeatherResponse {
        return parseResponseIntoWeather(jsonData)
    }
}