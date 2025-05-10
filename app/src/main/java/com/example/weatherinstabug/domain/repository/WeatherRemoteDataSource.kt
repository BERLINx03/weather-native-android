package com.example.weatherinstabug.domain.repository

import java.net.URL

interface WeatherRemoteDataSource {
    fun fetchWeatherData(url: URL): Result<String>
}