package com.example.weatherinstabug.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weatherinstabug.data.WeatherResponse
import com.example.weatherinstabug.presentation.ui.components.WeatherForecast
import com.example.weatherinstabug.presentation.ui.components.WeatherScreen
import com.example.weatherinstabug.utils.Screens

@Composable
fun WeatherApp(
    modifier: Modifier = Modifier,
    weatherResponse: WeatherResponse
) {
   val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.WeatherScreen,
        modifier = modifier
    ){
        composable(Screens.WeatherScreen){
            WeatherScreen()
        }
        composable(Screens.FiveDayForecastScreen){
            WeatherForecast()
        }
    }
}