package com.example.weatherinstabug.domain.repository

interface WeatherLocalDataSource {
    fun saveWeatherData(data: String)
    fun getCachedWeatherData(): String?
}