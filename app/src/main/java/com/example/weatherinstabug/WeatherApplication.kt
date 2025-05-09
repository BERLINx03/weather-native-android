package com.example.weatherinstabug

import android.app.Application

class WeatherApplication: Application() {
    /**
     * to keep it singleton for the whole application
     */
    companion object {

    }

    override fun onCreate() {
        super.onCreate()

    }
}