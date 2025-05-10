package com.example.weatherinstabug.data.repository

import android.os.Handler
import android.os.Looper
import com.example.weatherinstabug.domain.repository.WeatherCallback
import com.example.weatherinstabug.domain.repository.WeatherDataParser
import com.example.weatherinstabug.domain.repository.WeatherLocalDataSource
import com.example.weatherinstabug.domain.repository.WeatherRemoteDataSource
import com.example.weatherinstabug.domain.repository.WeatherRepository
import com.example.weatherinstabug.presentation.mapper.WeatherMapper
import com.example.weatherinstabug.utils.Constants
import java.net.URL
import java.time.LocalDate
import java.util.concurrent.Executors

class WeatherRepositoryImpl(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource,
    private val parser: WeatherDataParser
) : WeatherRepository {

    private val backgroundExecutor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    override fun shutdownExecutor() {
        backgroundExecutor.shutdown()
    }

    override fun fetchCurrentWeather(coordinates: Pair<Double, Double>, callback: WeatherCallback) {
        val url = URL("${Constants.BASE_URL}${coordinates.first},${coordinates.second}?key=${Constants.API_KEY}")
        fetchWeather(url, callback)
    }

    override fun fetchFiveDaysForecastWeather(coordinates: Pair<Double, Double>, callback: WeatherCallback) {
        val today = LocalDate.now()
        val lastDay = today.plusDays(4)
        val url = URL("${Constants.BASE_URL}${coordinates.first},${coordinates.second}/$today/$lastDay?key=${Constants.API_KEY}")
        fetchWeather(url, callback)
    }

    private fun fetchWeather(url: URL, callback: WeatherCallback) {
        backgroundExecutor.execute {
            val result = remoteDataSource.fetchWeatherData(url)

            result.fold(
                onSuccess = { jsonData ->
                    localDataSource.saveWeatherData(jsonData)
                    try {
                        val weatherResponse = parser.parseWeatherData(jsonData)

                        val weather = WeatherMapper.responseToDomain(weatherResponse)
                        handler.post { callback.onWeatherDataReceived(weather) }
                    } catch (e: Exception) {
                        handler.post { callback.onError("Failed to parse weather data: ${e.message}") }
                    }
                },
                onFailure = { error ->
                    val cachedData = localDataSource.getCachedWeatherData()
                    if (cachedData != null) {
                        try {
                            val weatherResponse = parser.parseWeatherData(cachedData)
                            val weather = WeatherMapper.responseToDomain(weatherResponse)
                            handler.post { callback.onWeatherDataReceived(weather) }
                        } catch (e: Exception) {
                            handler.post { callback.onError("Failed to parse cached data: ${e.message}") }
                        }
                    } else {
                        handler.post { callback.onError("Network error: ${error.message}") }
                    }
                }
            )
        }
    }
}