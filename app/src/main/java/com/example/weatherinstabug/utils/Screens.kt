package com.example.weatherinstabug.utils

sealed class Screens(val route: String) {
    data object WeatherScreen: Screens("weather_screen")
    data object FiveDayForecastScreen: Screens("five_day_forecast_screen")
}