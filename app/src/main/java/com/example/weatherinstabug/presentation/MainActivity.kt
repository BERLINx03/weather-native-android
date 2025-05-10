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
import com.example.weatherinstabug.domain.repository.WeatherRepository
import com.example.weatherinstabug.presentation.state.WeatherScreenState
import com.example.weatherinstabug.presentation.ui.WeatherApp
import com.example.weatherinstabug.presentation.ui.components.PermissionScreen
import com.example.weatherinstabug.utils.LocationUtils

class MainActivity : ComponentActivity(), WeatherController.StateCallback {

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

    private var weatherController: WeatherController? = null

    private var screenState by mutableStateOf<WeatherScreenState>(WeatherScreenState.Loading)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = (application as WeatherApplication).appContainer.weatherRepository
        val controller = WeatherController(applicationContext, repository)
        controller.setCallback(this)
        weatherController = controller

        if (savedInstanceState != null) {
            val savedWeather = savedInstanceState.getParcelable<WeatherResponse>("weatherState")
            if (savedWeather != null) {
                screenState = WeatherScreenState.Success(savedWeather)
            }
        } else {
            if (!hasRequiredPermissions()) {
                screenState = WeatherScreenState.PermissionRequired
                ActivityCompat.requestPermissions(this, requiredPermission, PERMISSION_REQUEST_CODE)
            } else {
                weatherController?.fetchWeather()
            }
        }

        setContent {
            Log.i("Weather", "Current state: $screenState")
            when (val state = screenState) {
                is WeatherScreenState.Loading -> LoadingScreen()
                is WeatherScreenState.Error -> ErrorScreen(state.message) {
                    weatherController?.fetchWeather()
                }
                is WeatherScreenState.Success -> WeatherApp(weatherResponse = state.data)
                is WeatherScreenState.PermissionRequired -> PermissionScreen {
                    ActivityCompat.requestPermissions(
                        this, requiredPermission, PERMISSION_REQUEST_CODE
                    )
                }
            }
        }
    }

    override fun onStateChanged(state: WeatherScreenState) {
        screenState = state
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)} passing\n      in a {@link RequestMultiplePermissions} object for the {@link ActivityResultContract} and\n      handling the result in the {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                weatherController?.fetchWeather()
            } else {
                screenState = WeatherScreenState.Error("Location permissions are required")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (screenState is WeatherScreenState.Success) {
            outState.putParcelable("weatherState", (screenState as WeatherScreenState.Success).data)
        }
    }

    override fun onDestroy() {
        weatherController?.clear()
        weatherController = null
        super.onDestroy()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
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