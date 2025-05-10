package com.example.weatherinstabug.presentation.state

import com.example.weatherinstabug.presentation.model.WeatherUi

sealed class WeatherScreenState {
    object Loading : WeatherScreenState()
    data class Error(val message: String) : WeatherScreenState()
    data class Success(val data: WeatherUi) : WeatherScreenState()
    object PermissionRequired : WeatherScreenState()
}