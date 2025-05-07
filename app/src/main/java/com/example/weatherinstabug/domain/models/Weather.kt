package com.example.weatherinstabug.domain.models

data class Weather(
    val location: String,
    val currentConditions: CurrentConditions,
    val hourlyForecast: List<HourForecast>,
    val dailyForecast: List<DayForecast>,
    val additionalInfo: AdditionalInfo
)

/**
 * Current weather conditions
 */
data class CurrentConditions(
    val temp: Int,               // Current temperature (e.g., 20°C)
    val tempMax: Int,            // Max temperature of the day (e.g., 33°)
    val tempMin: Int,            // Min temperature of the day (e.g., 16°)
    val conditions: String,      // Weather conditions description (e.g., "Clear")
    val windSpeed: Double,       // Wind speed (e.g., 3.7km/h)
    val windDirection: String,   // Wind direction (e.g., "East")
    val humidity: Int,           // Humidity percentage (e.g., 63%)
    val realFeel: Int,           // Real feel temperature (e.g., 19°)
    val uv: Int,                 // UV index (e.g., 0)
    val pressure: Int,           // Atmospheric pressure (e.g., 1017 mbar)
    val chanceOfRain: Int,       // Chance of rain percentage (e.g., 0%)
    val aqi: Int                 // Air Quality Index (e.g., 26)
)

/**
 * Day forecast information
 */
data class DayForecast(
    val day: String,             // Day of week (e.g., "Wed", "Thu", "Fri")
    val date: String,            // Date (e.g., "5/7", "5/8", "5/9")
    val tempMax: Int,            // Maximum temperature (e.g., 33°)
    val tempMin: Int,            // Minimum temperature (e.g., 16°)
    val conditions: String,      // Weather condition (e.g., "Clear", "Cloudy")
    val dayIcon: String,         // Icon representing day conditions
    val nightIcon: String,       // Icon representing night conditions
    val windSpeed: Double        // Wind speed (e.g., 3.7km/h)
)

/**
 * Hourly forecast information
 */
data class HourForecast(
    val hour: String,            // Hour of day (e.g., "Now", "08:00", "09:00")
    val temp: Int,               // Temperature (e.g., 20°)
    val icon: String,            // Weather condition icon
    val windSpeed: Double        // Wind speed (e.g., 3.7km/h)
)

/**
 * Additional weather information
 */
data class AdditionalInfo(
    val sunrise: String,         // Sunrise time (e.g., "06:07")
    val sunset: String,          // Sunset time (e.g., "19:36")
    val aqiLabel: String         // AQI description (shown as "AQI 26")
)
