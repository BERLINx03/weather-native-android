package com.example.weatherinstabug.data.remote

import com.example.weatherinstabug.domain.models.Weather
import com.example.weatherinstabug.utils.Constants

class WeatherRemoteDataSource : IWeatherRemote {
    private val baseUrl = Constants.BASE_URL
    private val key = Constants.API_KEY

    override fun fetchCurrentWeather(coordinates: Pair<Double, Double>): Weather {
        TODO("Not yet implemented")
    }

    override fun fetchFiveDaysForecastWeather(coordinates: Pair<Double, Double>): Weather {
        TODO("Not yet implemented")
    }


}