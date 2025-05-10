package com.example.weatherinstabug.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import com.example.weatherinstabug.WeatherApplication
import com.example.weatherinstabug.presentation.model.WeatherUi
import com.example.weatherinstabug.presentation.state.WeatherScreenState
import com.example.weatherinstabug.presentation.ui.WeatherApp
import com.example.weatherinstabug.presentation.ui.components.ErrorScreen
import com.example.weatherinstabug.presentation.ui.components.LoadingScreen
import com.example.weatherinstabug.presentation.ui.components.PermissionScreen

class MainActivity : ComponentActivity(), WeatherController.StateCallback, PermissionsHandler.PermissionCallback {

    private var weatherController: WeatherController? = null
    private var permissionsHandler: PermissionsHandler? = null

    private var screenState by mutableStateOf<WeatherScreenState>(WeatherScreenState.Loading)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = (application as WeatherApplication).appContainer.weatherRepository
        weatherController = WeatherController(applicationContext, repository)
        weatherController?.registerCallback(this)

        permissionsHandler = PermissionsHandler(this)

        if (savedInstanceState != null) {
            val savedWeather: WeatherUi? = savedInstanceState.getParcelable("weatherState")
            if (savedWeather != null) {
                screenState = WeatherScreenState.Success(savedWeather)
            }
        } else {
            if (permissionsHandler?.hasPermissions(PermissionsHandler.WEATHER_APP_PERMISSIONS) == true) {
                weatherController?.fetchWeather()
            } else {
                screenState = WeatherScreenState.PermissionRequired
                permissionsHandler?.requestPermissions(
                    PermissionsHandler.WEATHER_APP_PERMISSIONS,
                    PERMISSION_REQUEST_CODE
                )
            }
        }

        setContent {
            when (val state = screenState) {
                is WeatherScreenState.Loading -> LoadingScreen()
                is WeatherScreenState.Error -> ErrorScreen(state.message) {
                    weatherController?.fetchWeather()
                }
                is WeatherScreenState.Success -> WeatherApp(weatherUi = state.data)
                is WeatherScreenState.PermissionRequired -> PermissionScreen {
                    ActivityCompat.requestPermissions(
                        this, PermissionsHandler.WEATHER_APP_PERMISSIONS, PERMISSION_REQUEST_CODE
                    )
                }
            }
        }
    }

    override fun onStateChanged(state: WeatherScreenState) {
        screenState = state
    }


    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            weatherController?.fetchWeather()
        } else {
            screenState = WeatherScreenState.Error("Location permissions are required")
        }
    }

    /**
     * ## `Deprecated` but i use it cause my phone doesn't support the result api (API 30+)
     */
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)} passing\n      in a {@link RequestMultiplePermissions} object for the {@link ActivityResultContract} and\n      handling the result in the {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            permissionsHandler?.handlePermissionResult(grantResults, this)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (screenState is WeatherScreenState.Success) {
            outState.putParcelable("weatherState", (screenState as WeatherScreenState.Success).data)
        }
    }

    /**
     * ### Cleaning resources and removing pointers
     */
    override fun onDestroy() {
        super.onDestroy()

        weatherController?.clear()
        weatherController = null

        permissionsHandler?.clear()
        permissionsHandler = null
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
}