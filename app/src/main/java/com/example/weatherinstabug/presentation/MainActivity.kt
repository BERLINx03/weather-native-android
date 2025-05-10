package com.example.weatherinstabug.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherinstabug.WeatherApplication
import com.example.weatherinstabug.data.WeatherResponse
import com.example.weatherinstabug.domain.repository.WeatherCallback
import com.example.weatherinstabug.presentation.ui.WeatherApp
import com.example.weatherinstabug.utils.LocationUtils

class MainActivity : ComponentActivity() {

    private val requiredPermission = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.INTERNET,
        android.Manifest.permission.ACCESS_NETWORK_STATE
    )

    private fun hasRequiredPermissions(): Boolean {
        return requiredPermission.all {
            ContextCompat.checkSelfPermission(
                this,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private var weatherState by mutableStateOf<WeatherResponse?>(null)
    private var locationState by mutableStateOf<Pair<Double, Double>?>(null)
    private var isLoading by mutableStateOf(true)
    private var errorMessage by mutableStateOf<String?>(null)

    private val weatherRepository = WeatherApplication.appModule.weatherRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            weatherState = savedInstanceState.getParcelable("weatherState")
            isLoading = false
        } else {
            if (!hasRequiredPermissions()) {
                ActivityCompat.requestPermissions(
                    this,
                    requiredPermission,
                    0
                )
            } else {
                initializeLocationAndWeather()
            }
        }

        setContent {
            Log.i("Weather", "my response is : $weatherState")
            when {
                isLoading -> LoadingScreen()
                errorMessage != null -> ErrorScreen(errorMessage!!) {
                    errorMessage = null
                    isLoading = true
                    initializeLocationAndWeather()
                }
                weatherState != null -> WeatherApp(weatherResponse = weatherState!!)
                else -> LoadingScreen()
            }
        }
    }

    private fun initializeLocationAndWeather() {
        if (hasRequiredPermissions()) {
            Log.d("MainActivity", "Permissions granted, initializing location")
            try {
                LocationUtils(this, object : LocationUtils.LocationCallback {
                    override fun onLocationReceived(coordinates: Pair<Double, Double>) {
                        Log.d("MainActivity", "Location received: $coordinates")
                        locationState = coordinates
                        fetchWeather(coordinates)
                    }

                    override fun onLocationError(errorMessage: String) {
                        Log.e("MainActivity", "Location error: $errorMessage")
                        this@MainActivity.errorMessage = errorMessage
                        isLoading = false
                    }
                })
            } catch (e: Exception) {
                Log.e("MainActivity", "Error initializing location: ${e.message}")
                errorMessage = "Error initializing location services: ${e.message}"
                isLoading = false
            }
        } else {
            Log.e("MainActivity", "Permissions still not available")
            errorMessage = "Location permissions are required"
            isLoading = false
        }
    }

    private fun fetchWeather(coordinates: Pair<Double, Double>) {
        Log.d("MainActivity", "Fetching weather for coordinates: $coordinates")
        weatherRepository.fetchCurrentWeather(
            coordinates = coordinates,
            callback = object : WeatherCallback {
                override fun onWeatherDataReceived(weatherResponse: WeatherResponse) {
                    Log.d("MainActivity", "Weather data received")
                    weatherState = weatherResponse
                    isLoading = false
                }

                override fun onError(errorMessage: String) {
                    Log.e("MainActivity", "Weather fetch error: $errorMessage")
                    this@MainActivity.errorMessage = errorMessage
                    isLoading = false
                }
            }
        )
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API...")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("MainActivity", "Permission result: ${grantResults.joinToString()}")
        if (requestCode == 0 && grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            Log.d("MainActivity", "All permissions granted, initializing")
            initializeLocationAndWeather()
        } else {
            Log.e("MainActivity", "Permissions denied or incomplete")
            errorMessage = "Location permissions denied"
            isLoading = false
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

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(errorMessage: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error: $errorMessage",
            color = Color.Red,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}