package com.example.weatherinstabug.utils

fun Double.toCelsius(): Double{
    return (this - 32) / 1.8
}

fun Double.toFahrenheit(): Double{
    return (this * 1.8) + 32
}
