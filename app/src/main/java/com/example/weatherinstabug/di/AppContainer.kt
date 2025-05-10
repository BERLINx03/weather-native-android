package com.example.weatherinstabug.di

import android.content.Context
import android.net.ConnectivityManager
import com.example.weatherinstabug.data.local.WeatherCache
import com.example.weatherinstabug.data.repository.WeatherDataParserImpl
import com.example.weatherinstabug.data.repository.WeatherLocalDataSourceImpl
import com.example.weatherinstabug.data.repository.WeatherRemoteDataSourceImpl
import com.example.weatherinstabug.data.repository.WeatherRepositoryImpl
import com.example.weatherinstabug.domain.repository.WeatherRepository
import com.example.weatherinstabug.utils.NetworkUtils

class AppContainer(private val context: Context) {
    private val networkUtils by lazy {
        NetworkUtils(context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    }

    private val weatherCache by lazy { WeatherCache.apply { init(context) } }

    private val weatherRemoteDataSource by lazy { WeatherRemoteDataSourceImpl(networkUtils) }

    private val weatherLocalDataSource by lazy { WeatherLocalDataSourceImpl(weatherCache) }

    private val weatherDataParser by lazy { WeatherDataParserImpl() }

    val weatherRepository: WeatherRepository by lazy {
        WeatherRepositoryImpl(
            remoteDataSource = weatherRemoteDataSource,
            localDataSource = weatherLocalDataSource,
            parser = weatherDataParser
        )
    }
}