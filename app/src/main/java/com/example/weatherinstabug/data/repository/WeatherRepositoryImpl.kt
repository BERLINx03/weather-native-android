package com.example.weatherinstabug.data.repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.weatherinstabug.utils.Constants
import org.json.JSONException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.util.concurrent.Executors
import com.example.weatherinstabug.domain.repository.WeatherCallback
import com.example.weatherinstabug.domain.repository.WeatherRepository
import com.example.weatherinstabug.utils.parseResponseIntoWeather

class WeatherRepositoryImpl() : WeatherRepository {
    lateinit var conn: HttpURLConnection
    private val baseUrl = Constants.BASE_URL
    private val key = Constants.API_KEY

    private val today = LocalDate.now()
    private val lastDay = today.plusDays(4)

    private val backgroundExecutor = Executors.newSingleThreadExecutor()

    private val mainLooper = Looper.getMainLooper()
    private val handler = Handler(mainLooper)

    override fun shutdownExecutor() {
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
                handler.post {
//                    sharedPref.edit().apply{
//                        putString("weather", response)
//                        apply()
//                    }
                    callback.onWeatherDataReceived(parseResponseIntoWeather(response))
                }
            } catch (e: IOException) {
                Log.e("WEATHER_API", "Network error: ${e.localizedMessage}", e)
                handler.post {
                    callback.onError("Network error: ${e.localizedMessage}")
                }
            } catch (e: JSONException) {
                Log.e("WEATHER_API", "JSON parsing error: ${e.localizedMessage}", e)
                handler.post {
                    callback.onError("Failed to parse weather data")
                }
            } catch (e: Exception) {
                Log.e("WEATHER_API", "Unknown error: ${e.localizedMessage}", e)
                handler.post {
                    callback.onError("An unexpected error occurred")
                }
            } finally {
                conn.disconnect()
            }
        }
    }
}