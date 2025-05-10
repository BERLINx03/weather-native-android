package com.example.weatherinstabug.data

data class WeatherResponseDto(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val resolvedAddress: String,
    val description: String,
    val currentConditionsDto: CurrentConditionsDto,
    val tzoffset: Int,
    val dayDtos: List<DayDto>
)

data class CurrentConditionsDto(
    val datetime: String,
    val temp: Double,
    val feelslike: Double,
    val humidity: Double,
    val dew: Double,
    val uvindex: Int,
    val pressure: Double,
    val windspeed: Double,
    val winddir: Double,
    val conditions: String,
    val icon: String,
    val sunrise: String,
    val sunset: String,
    val precipprob: Double
)

data class DayDto(
    val datetime: String,
    val tempmax: Double,
    val tempmin: Double,
    val temp: Double,
    val conditions: String,
    val icon: String,
    val precipprob: Double,
    val windspeed: Double,
    val winddir: String,
    val hourDtos: List<HourDto>
)

data class HourDto(
    val datetime: String,
    val temp: Double,
    val windspeed: Double,
    val icon: String,
    val precipprob: Double
)