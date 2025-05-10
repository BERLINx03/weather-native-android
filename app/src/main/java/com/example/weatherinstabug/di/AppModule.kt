package com.example.weatherinstabug.di

import android.content.Context
import com.example.weatherinstabug.data.local.WeatherCache
import com.example.weatherinstabug.data.repository.WeatherDataParserImpl
import com.example.weatherinstabug.data.repository.WeatherLocalDataSourceImpl
import com.example.weatherinstabug.data.repository.WeatherRemoteDataSourceImpl
import com.example.weatherinstabug.data.repository.WeatherRepositoryImpl
import com.example.weatherinstabug.domain.repository.WeatherDataParser
import com.example.weatherinstabug.domain.repository.WeatherLocalDataSource
import com.example.weatherinstabug.domain.repository.WeatherRemoteDataSource
import com.example.weatherinstabug.domain.repository.WeatherRepository
import com.example.weatherinstabug.utils.NetworkUtils

interface AppModule {
    val weatherRepository: WeatherRepository
}

class AppModuleImpl(
    networkUtils: NetworkUtils,
    weatherCache: WeatherCache
) : AppModule {

    private val weatherDataParser: WeatherDataParser = WeatherDataParserImpl()

    private val weatherRemoteDataSource: WeatherRemoteDataSource =
        WeatherRemoteDataSourceImpl(networkUtils)

    private val weatherLocalDataSource: WeatherLocalDataSource =
        WeatherLocalDataSourceImpl(weatherCache)

    override val weatherRepository: WeatherRepository by lazy {
        WeatherRepositoryImpl(
            remoteDataSource = weatherRemoteDataSource,
            localDataSource = weatherLocalDataSource,
            parser = weatherDataParser
        )
    }
}