package com.example.weatherinstabug

import android.app.Application
import com.example.weatherinstabug.di.AppContainer

class WeatherApplication: Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}