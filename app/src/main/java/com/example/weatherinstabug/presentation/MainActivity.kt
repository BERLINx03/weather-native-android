package com.example.weatherinstabug.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherinstabug.data.WeatherResponse
import com.example.weatherinstabug.data.remote.WeatherCallback
import com.example.weatherinstabug.data.remote.WeatherRemoteDataSource
import com.example.weatherinstabug.presentation.ui.theme.WeatherInstabugTheme
import com.example.weatherinstabug.utils.LocationUtils

class MainActivity : ComponentActivity() {
    val weather = WeatherRemoteDataSource()

    var weatherResponse = mutableStateOf<WeatherResponse?>(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                weatherResponse.value =
                    savedInstanceState.getParcelable("Weather", WeatherResponse::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                weatherResponse.value =
                    savedInstanceState.getParcelable<WeatherResponse>("Weather")!!
            }
        }
        LocationUtils(this, object : LocationUtils.LocationCallback {
            override fun onLocationReceived(coordinates: Pair<Double, Double>) {
                weather.fetchFiveDaysForecastWeather(coordinates, object : WeatherCallback {
                    override fun onWeatherDataReceived(weather: WeatherResponse) {
                        weatherResponse.value = weather
                        Log.d("Weather, MainActivity", "onWeatherDataReceived: $weather")
                    }

                    override fun onError(errorMessage: String) {

                    }
                })
            }
        })
        val requiredPermission = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE
        )

        val hasRequiredPermissions = {
            requiredPermission.all {
                ContextCompat.checkSelfPermission(
                    this,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this,
                requiredPermission,
                0
            )
        }

        enableEdgeToEdge()
        setContent {
            WeatherInstabugTheme {
                WeatherScreen(weatherState = weatherResponse)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("Weather", weatherResponse.value)
    }

    override fun onDestroy() {
        super.onDestroy()
        weather.shutdownExecutor
    }
}

@Composable
fun WeatherScreen(weatherState: MutableState<WeatherResponse?>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Weather App", style = MaterialTheme.typography.headlineMedium)

        val weatherData = weatherState.value

        if (weatherData == null) {
            // Show loading indicator when weather data is null
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            Text(
                "Loading weather data...",
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            // Display weather data safely
            Text(
                text = weatherData.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Current conditions: ${weatherData.currentConditions.conditions}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}