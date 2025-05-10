package com.example.weatherinstabug.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.weatherinstabug.data.WeatherResponse
import com.example.weatherinstabug.data.repository.WeatherRepositoryImpl
import com.example.weatherinstabug.domain.repository.WeatherCallback
import com.example.weatherinstabug.presentation.ui.WeatherApp
import com.example.weatherinstabug.presentation.ui.components.WeatherForecast
import com.example.weatherinstabug.presentation.ui.components.WeatherScreen

class MainActivity : ComponentActivity() {
    private var weatherState by mutableStateOf<WeatherResponse?>(null)
    private val weather = WeatherRepositoryImpl().fetchCurrentWeather(
        Pair(40.7128, -74.0060),
        object : WeatherCallback{
            override fun onWeatherDataReceived(weather: WeatherResponse) {
                weatherState = weather
            }

            override fun onError(errorMessage: String) {
                TODO("Not yet implemented")
            }
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (weatherState != null) {
                WeatherApp(weatherResponse = weatherState!!)
            }
        }
    }
}