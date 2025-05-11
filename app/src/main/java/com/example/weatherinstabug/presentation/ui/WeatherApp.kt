package com.example.weatherinstabug.presentation.ui

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherinstabug.presentation.model.WeatherUi
import com.example.weatherinstabug.presentation.ui.components.WeatherForecast
import com.example.weatherinstabug.presentation.ui.components.WeatherScreen
import com.example.weatherinstabug.utils.Screens

@Composable
fun WeatherApp(
    modifier: Modifier = Modifier,
    weatherUi: WeatherUi,
    onRefresh: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.WeatherScreen.route,
        modifier = modifier,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { -it } },
        popEnterTransition = { slideInHorizontally { -it } },
        popExitTransition = { slideOutHorizontally { it } }
    ) {
        composable(Screens.WeatherScreen.route) {
            WeatherScreen(
                weatherUi,
                onForecastClick = {
                    navController.navigate(Screens.FiveDayForecastScreen.route)
                },
                onRefresh = {
                    onRefresh()
                }
            )
        }
        composable(Screens.FiveDayForecastScreen.route) {
            WeatherForecast(
                weatherUi,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}