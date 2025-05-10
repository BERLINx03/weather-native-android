package com.example.weatherinstabug.data.repository

import com.example.weatherinstabug.domain.repository.WeatherRemoteDataSource
import com.example.weatherinstabug.utils.NetworkUtils
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class WeatherRemoteDataSourceImpl(
    private val networkUtils: NetworkUtils
) : WeatherRemoteDataSource {
    override fun fetchWeatherData(url: URL): Result<String> {
        if (!networkUtils.hasValidInternet()) {
            return Result.failure(IOException("No internet connection"))
        }

        var connection: HttpURLConnection? = null
        return try {
            connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Accept", "application/json")
                doInput = true
            }

            val response = connection.inputStream.bufferedReader().use { it.readText() }
            Result.success(response)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            connection?.disconnect()
        }
    }
}