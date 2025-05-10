package com.example.weatherinstabug.domain.repository

import com.example.weatherinstabug.domain.model.Weather

interface WeatherCallback {
    fun onWeatherDataReceived(weather: Weather)
    fun onError(errorMessage: String)
}