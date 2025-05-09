package com.example.weatherinstabug.domain.repository

import com.example.weatherinstabug.data.WeatherResponse

interface WeatherCallback {
    fun onWeatherDataReceived(weather: WeatherResponse)
    fun onError(errorMessage: String)
}