package com.example.weatherinstabug.domain.repository


interface WeatherRepository {
    fun fetchCurrentWeather(callback: WeatherCallback)
    fun fetchFiveDaysForecastWeather(callback: WeatherCallback)
    fun shutdownExecutor()
}