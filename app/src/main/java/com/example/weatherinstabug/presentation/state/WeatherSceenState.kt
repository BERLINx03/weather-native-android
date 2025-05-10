package com.example.weatherinstabug.presentation.state

import com.example.weatherinstabug.data.WeatherResponse

sealed class WeatherScreenState {
    object Loading : WeatherScreenState()
    data class Error(val message: String) : WeatherScreenState()
    data class Success(val data: WeatherResponse) : WeatherScreenState()
    object PermissionRequired : WeatherScreenState()
}