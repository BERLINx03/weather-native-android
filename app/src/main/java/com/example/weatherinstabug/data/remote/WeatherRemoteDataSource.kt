package com.example.weatherinstabug.data.remote

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.weatherinstabug.data.CurrentConditions
import com.example.weatherinstabug.data.Day
import com.example.weatherinstabug.data.Hour
import com.example.weatherinstabug.data.WeatherResponse
import com.example.weatherinstabug.utils.Constants
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.util.concurrent.Executors

class WeatherRemoteDataSource : IWeatherRemote {
    lateinit var conn: HttpURLConnection
    private val baseUrl = Constants.BASE_URL
    private val key = Constants.API_KEY

    private val today = LocalDate.now()
    private val lastDay = today.plusDays(4)

    private val backgroundExecutor = Executors.newSingleThreadExecutor()

    private val mainLooper = Looper.getMainLooper()

    val shutdownExecutor = {
        backgroundExecutor.shutdown()
    }

    override fun fetchCurrentWeather(coordinates: Pair<Double, Double>, callback: WeatherCallback) {
        val url = URL("$baseUrl${coordinates.first},${coordinates.second}?key=$key")
        fetchWeather(url, callback)
    }

    override fun fetchFiveDaysForecastWeather(coordinates: Pair<Double, Double>, callback: WeatherCallback) {
        val url = URL("$baseUrl${coordinates.first},${coordinates.second}/$today/$lastDay?key=$key")
        fetchWeather(url,callback)
    }

    private fun fetchWeather(url: URL, callback: WeatherCallback){
        backgroundExecutor.execute {
            try {
                conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("Accept", "application/json")
                    doInput = true
                }

                val response = conn.inputStream.bufferedReader().use {
                    it.readText()
                }

                Log.i("WeatherRemote", "my response is : ${parseResponseIntoWeather(response)}")
                Handler(mainLooper).post {
                    callback.onWeatherDataReceived(parseResponseIntoWeather(response))
                }
            } catch (e: IOException) {
                Log.e("WEATHER_API", "Network error: ${e.localizedMessage}", e)
                Handler(Looper.getMainLooper()).post {
                    callback.onError("Network error: ${e.localizedMessage}")
                }
            } catch (e: JSONException) {
                Log.e("WEATHER_API", "JSON parsing error: ${e.localizedMessage}", e)
                Handler(Looper.getMainLooper()).post {
                    callback.onError("Failed to parse weather data")
                }
            } catch (e: Exception) {
                Log.e("WEATHER_API", "Unknown error: ${e.localizedMessage}", e)
                Handler(Looper.getMainLooper()).post {
                    callback.onError("An unexpected error occurred")
                }
            } finally {
                conn.disconnect()
            }
        }
    }

    private fun Double.toCelsius(): Double{
        return (this - 32) / 1.8
    }
    /*
    {
  "latitude": number,  // Not directly shown, used for location
  "longitude": number, // Not directly shown, used for location
  "timezone": string,  // Not shown in UI
  "resolvedAddress": "30.1107574,31.2969563",
  "description": string,  // Overall forecast description
  "currentConditions": {
    "datetime": string,  // Related to 6:09 AM in status bar
    "temp": number,  // "20°C" in image 2
    "feelslike": number,  // "19°" as "Real feel" in image 3
    "humidity": number,  // "63%" in image 3
    "dew": number,  // Dew point is the temperature at which air becomes saturated with water vapor, causing condensation to form
    "uvindex": number,  // "0" in image 3
    "pressure": number,  // "1017 mbar" in image 3
    "windspeed": number,  // "3.7km/h" in image 3
    "winddir": number,  // Shown as "East" with compass in image 3
    "conditions": string,  // "Clear" in image 2
    "icon": string,  // Sun icon in images
    "sunrise": string,  // "06:07" in image 3
    "sunset": string,  // "19:36" in image 3
    "precipprob": number  // "0%" as "Chance of rain" in image 3
  },
  "days": [
    {
      "datetime": string,  // "Tue 5/6", "Wed 5/7", etc. in image 1
      "tempmax": number,  // "33°" for Wednesday in image 2
      "tempmin": number,  // "16°" for Wednesday in image 2
      "conditions": string,  // "Clear" for Wednesday in image 2
      "icon": string,  // Sun/cloud icons in images
      "precipprob": number,  // Related to chance of rain
      "windspeed": number,  // "9.3km/h", "3.7km/h", etc. in image 1
      "winddir": number,  // Direction in degrees
      "hours": [
        {
          "datetime": string,  // "Now", "08:00", "09:00" etc. in image 3
          "temp": number,  // "20°", "20°", "23°" etc. in image 3
          "windspeed": number,  // "3.7km/h", "13.0km/h" etc. in image 3
          "icon": string  // Sun icons in image 3
        }
      ]
    }
  ]
}
    */
    private fun parseResponseIntoWeather(response: String): WeatherResponse {
        val jsonObj = JSONObject(response)
        val latitude = jsonObj.getDouble("latitude")
        val longitude = jsonObj.getDouble("longitude")
        val timezone = jsonObj.getString("timezone")
        val resolvedAddress = jsonObj.getString("resolvedAddress")
        val description = jsonObj.getString("description")

        // Parse current conditions
        val currentConditions = jsonObj.getJSONObject("currentConditions")
        val current = CurrentConditions(
            datetime = currentConditions.getString("datetime"),
            temp = currentConditions.getDouble("temp").toCelsius() ,
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
                        icon = hourObj.getString("icon")
                    )
                )
            }

            days.add(
                Day(
                    datetime = dayObj.getString("datetime"),
                    tempmax = dayObj.getDouble("tempmax").toCelsius(),
                    tempmin = dayObj.getDouble("tempmin").toCelsius(),
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
            days = days
        )
    }
    private fun getWindDirectionText(degrees: Double?): String {
        if (degrees == null) return ""

        val normalizedDegrees = ((degrees % 360) + 360) % 360

        return when {
            normalizedDegrees >= 348.75 || normalizedDegrees < 11.25 -> "N"
            normalizedDegrees < 33.75 -> "NNE"
            normalizedDegrees < 56.25 -> "NE"
            normalizedDegrees < 78.75 -> "ENE"
            normalizedDegrees < 101.25 -> "E"
            normalizedDegrees < 123.75 -> "ESE"
            normalizedDegrees < 146.25 -> "SE"
            normalizedDegrees < 168.75 -> "SSE"
            normalizedDegrees < 191.25 -> "S"
            normalizedDegrees < 213.75 -> "SSW"
            normalizedDegrees < 236.25 -> "SW"
            normalizedDegrees < 258.75 -> "WSW"
            normalizedDegrees < 281.25 -> "W"
            normalizedDegrees < 303.75 -> "WNW"
            normalizedDegrees < 326.25 -> "NW"
            normalizedDegrees < 348.75 -> "NNW"
            else -> ""
        }
    }
}