package com.example.weatherinstabug.domain.repository


interface WeatherRepository {
    fun fetchCurrentWeather(coordinates: Pair<Double, Double>,callback: WeatherCallback)
    fun fetchFiveDaysForecastWeather(coordinates: Pair<Double, Double>,callback: WeatherCallback)
    fun shutdownExecutor()
}