package com.example.weatherinstabug.utils

import com.example.weatherinstabug.data.CurrentConditions
import com.example.weatherinstabug.data.Day
import com.example.weatherinstabug.data.Hour
import com.example.weatherinstabug.data.WeatherResponse
import org.json.JSONObject

fun parseResponseIntoWeather(response: String): WeatherResponse {
    val jsonObj = JSONObject(response)
    val latitude = jsonObj.getDouble("latitude")
    val longitude = jsonObj.getDouble("longitude")
    val timezone = jsonObj.getString("timezone")
    val resolvedAddress = jsonObj.getString("resolvedAddress")
    val description = jsonObj.getString("description")
    val tzoffset = jsonObj.getInt("tzoffset")

    // Parse current conditions
    val currentConditions = jsonObj.getJSONObject("currentConditions")
    val current = CurrentConditions(
        datetime = currentConditions.getString("datetime"),
        temp = currentConditions.getDouble("temp").toCelsius(),
        feelslike = currentConditions.getDouble("feelslike").toCelsius(),
        humidity = currentConditions.getDouble("humidity"),
        dew = currentConditions.getDouble("dew"),
        uvindex = currentConditions.getInt("uvindex"),
        pressure = currentConditions.getDouble("pressure"),
        windspeed = currentConditions.getDouble("windspeed"),
        winddir = currentConditions.getDouble("winddir"),
        conditions = currentConditions.getString("conditions"),
        icon = currentConditions.getString("icon"),
        sunrise = currentConditions.getString("sunrise"),
        sunset = currentConditions.getString("sunset"),
        precipprob = currentConditions.getDouble("precipprob")
    )

    val daysArray = jsonObj.getJSONArray("days")
    val days = mutableListOf<Day>()

    for (i in 0 until daysArray.length()) {
        val dayObj = daysArray.getJSONObject(i)

        val hoursArray = dayObj.getJSONArray("hours")
        val hours = mutableListOf<Hour>()

        for (j in 0 until hoursArray.length()) {
            val hourObj = hoursArray.getJSONObject(j)
            hours.add(
                Hour(
                    datetime = hourObj.getString("datetime"),
                    temp = hourObj.getDouble("temp").toCelsius(),
                    windspeed = hourObj.getDouble("windspeed"),
                    icon = hourObj.getString("icon"),
                    precipprob = hourObj.getDouble("precipprob")
                )
            )
        }

        days.add(
            Day(
                datetime = dayObj.getString("datetime"),
                tempmax = dayObj.getDouble("tempmax").toCelsius(),
                tempmin = dayObj.getDouble("tempmin").toCelsius(),
                temp = dayObj.getDouble("temp").toCelsius(),
                conditions = dayObj.getString("conditions"),
                icon = dayObj.getString("icon"),
                precipprob = dayObj.getDouble("precipprob"),
                windspeed = dayObj.getDouble("windspeed"),
                winddir = getWindDirectionText(dayObj.getDouble("winddir")),
                hours = hours
            )
        )
    }

    return WeatherResponse(
        latitude = latitude,
        longitude = longitude,
        timezone = timezone,
        resolvedAddress = resolvedAddress,
        description = description,
        currentConditions = current,
        days = days,
        tzoffset = tzoffset
    )
}
