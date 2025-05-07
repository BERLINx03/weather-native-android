package com.example.weatherinstabug.data.remote


interface IWeatherRemote {
    fun fetchCurrentWeather(coordinates: Pair<Double, Double>, callback: WeatherCallback)
    fun fetchFiveDaysForecastWeather(coordinates: Pair<Double, Double>, callback: WeatherCallback)
}