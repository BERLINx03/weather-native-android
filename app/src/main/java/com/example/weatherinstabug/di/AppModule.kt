package com.example.weatherinstabug.di

import android.content.Context
import com.example.weatherinstabug.data.local.WeatherCache
import com.example.weatherinstabug.data.repository.WeatherRepositoryImpl
import com.example.weatherinstabug.domain.repository.WeatherRepository
import com.example.weatherinstabug.utils.LocationUtils
import com.example.weatherinstabug.utils.NetworkUtils

interface AppModule {
    val weatherRepository: WeatherRepository
}

class AppModuleImpl(
    private val appCtx: Context,
    private val networkUtils: NetworkUtils,
    private val weatherCache: WeatherCache,
    private val coordinates: Pair<Double, Double>
): AppModule{
    override val weatherRepository: WeatherRepository by lazy {
        WeatherRepositoryImpl(
            context = appCtx,
            networkUtils = networkUtils,
            weatherCache = weatherCache,
            coordinates = coordinates
        )
    }
}