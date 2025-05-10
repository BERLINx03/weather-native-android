package com.example.weatherinstabug.data.local

import android.content.Context
import android.content.SharedPreferences

object WeatherCache {
    private const val PREF_NAME = "weather_pref"
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    var weather: String?
        get() = preferences.getString("weather", null)
        set(value) = preferences.edit().putString("weather", value).apply()
}
