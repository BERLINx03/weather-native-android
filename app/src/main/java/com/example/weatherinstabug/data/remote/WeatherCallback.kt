package com.example.weatherinstabug.data.remote

import com.example.weatherinstabug.data.WeatherResponse

interface WeatherCallback {
    fun onWeatherDataReceived(weather: WeatherResponse)
    fun onError(errorMessage: String)
}