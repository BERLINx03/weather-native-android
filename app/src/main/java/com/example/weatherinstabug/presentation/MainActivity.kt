package com.example.weatherinstabug.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherinstabug.WeatherApplication
import com.example.weatherinstabug.data.WeatherResponse
import com.example.weatherinstabug.domain.repository.WeatherCallback
import com.example.weatherinstabug.presentation.ui.WeatherApp

class MainActivity : ComponentActivity() {

    private val requiredPermission = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.INTERNET,
        android.Manifest.permission.ACCESS_NETWORK_STATE
    )

    private val hasRequiredPermissions = {
        requiredPermission.all {
            ContextCompat.checkSelfPermission(
                this,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private var weatherState by mutableStateOf<WeatherResponse?>(null)

    private val weatherRepository = WeatherApplication.appModule.weatherRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this,
                requiredPermission,
                0
            )
        }

        if (savedInstanceState != null) {
            weatherState = savedInstanceState.getParcelable("weatherState")
        } else {
            weatherRepository.fetchCurrentWeather(
                object : WeatherCallback {
                    override fun onWeatherDataReceived(weatherResponse: WeatherResponse) {
                        weatherState = weatherResponse
                    }
                    override fun onError(errorMessage: String) {

                    }
                }
            )
        }

        setContent {
            Log.i("Weather", "my response is : $weatherState")
            if (weatherState != null) {
                WeatherApp(weatherResponse = weatherState!!)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("weatherState", weatherState)
    }

    override fun onDestroy() {
        super.onDestroy()
        weatherRepository.shutdownExecutor()
    }
}