package com.example.weatherinstabug.domain.model

data class Weather(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val resolvedAddress: String,
    val description: String,
    val currentConditions: CurrentConditions,
    val tzOffset: Int,
    val days: List<Day>
)

data class CurrentConditions(
    val dateTime: String,
    val temp: Double,
    val feelsLike: Double,
    val humidity: Double,
    val dew: Double,
    val uvIndex: Int,
    val pressure: Double,
    val windSpeed: Double,
    val windDir: Double,
    val conditions: String,
    val icon: String,
    val sunrise: String,
    val sunset: String,
    val precipProb: Double
)

data class Day(
    val dateTime: String,
    val tempMax: Double,
    val tempMin: Double,
    val temp: Double,
    val conditions: String,
    val icon: String,
    val precipProb: Double,
    val windSpeed: Double,
    val windDir: String,
    val hours: List<Hour>
)

data class Hour(
    val dateTime: String,
    val temp: Double,
    val windSpeed: Double,
    val icon: String,
    val precipProb: Double
)