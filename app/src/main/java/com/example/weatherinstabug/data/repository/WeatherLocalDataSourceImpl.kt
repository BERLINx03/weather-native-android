package com.example.weatherinstabug.data.repository

import com.example.weatherinstabug.data.local.WeatherCache
import com.example.weatherinstabug.domain.repository.WeatherLocalDataSource

class WeatherLocalDataSourceImpl(
    private val weatherCache: WeatherCache
) : WeatherLocalDataSource {
    override fun saveWeatherData(data: String) {
        weatherCache.weather = data
    }

    override fun getCachedWeatherData(): String? {
        return weatherCache.weather
    }
}