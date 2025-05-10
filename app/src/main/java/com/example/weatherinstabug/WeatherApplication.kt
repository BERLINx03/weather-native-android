package com.example.weatherinstabug

import android.app.Application
import android.net.ConnectivityManager
import com.example.weatherinstabug.data.local.WeatherCache
import com.example.weatherinstabug.di.AppModule
import com.example.weatherinstabug.di.AppModuleImpl
import com.example.weatherinstabug.utils.NetworkUtils

class WeatherApplication: Application() {
    /**
     * to keep it singleton for the whole application
     */
    companion object {
        lateinit var appModule : AppModule
    }

    override fun onCreate() {
        super.onCreate()

        WeatherCache.init(this)

        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager


        appModule = AppModuleImpl(
            networkUtils = NetworkUtils(connectivityManager),
            weatherCache = WeatherCache,
        )
    }
}