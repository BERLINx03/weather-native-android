package com.example.weatherinstabug.data.remote

import com.example.weatherinstabug.domain.models.Weather

interface IWeatherRemote {
    fun fetchCurrentWeather(coordinates: Pair<Double, Double>): Weather
    fun fetchFiveDaysForecastWeather(coordinates: Pair<Double, Double>): Weather
}