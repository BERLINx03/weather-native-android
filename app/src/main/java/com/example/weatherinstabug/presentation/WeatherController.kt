package com.example.weatherinstabug.presentation

import android.content.Context
import com.example.weatherinstabug.data.WeatherResponse
import com.example.weatherinstabug.domain.repository.WeatherCallback
import com.example.weatherinstabug.domain.repository.WeatherRepository
import com.example.weatherinstabug.presentation.state.WeatherScreenState
import com.example.weatherinstabug.utils.LocationUtils

class WeatherController(
    private val appContext: Context,
    private val weatherRepository: WeatherRepository
) {
    interface StateCallback {
        fun onStateChanged(state: WeatherScreenState)
    }

    var callback: StateCallback? = null

    fun setCallback(callback: StateCallback) {
        this.callback = callback
    }

    fun fetchWeather() {
        callback?.onStateChanged(WeatherScreenState.Loading)

        try {
            LocationUtils(appContext, object : LocationUtils.LocationCallback {
                override fun onLocationReceived(coordinates: Pair<Double, Double>) {
                    weatherRepository.fetchCurrentWeather(
                        coordinates = coordinates,
                        callback = object : WeatherCallback {
                            override fun onWeatherDataReceived(weatherResponse: WeatherResponse) {
                                callback?.onStateChanged(WeatherScreenState.Success(weatherResponse))
                            }

                            override fun onError(errorMessage: String) {
                                callback?.onStateChanged(WeatherScreenState.Error(errorMessage))
                            }
                        }
                    )
                }

                override fun onLocationError(errorMessage: String) {
                    callback?.onStateChanged(WeatherScreenState.Error(errorMessage))
                }
            })
        } catch (e: Exception) {
            callback?.onStateChanged(
                WeatherScreenState.Error("Error initializing location: ${e.message}")
            )
        }
    }

    fun clear() {
        callback = null
        weatherRepository.shutdownExecutor()
    }
}