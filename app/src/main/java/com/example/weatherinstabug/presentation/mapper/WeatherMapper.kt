package com.example.weatherinstabug.presentation.mapper

import com.example.weatherinstabug.data.WeatherResponseDto
import com.example.weatherinstabug.domain.model.CurrentConditions
import com.example.weatherinstabug.domain.model.Day
import com.example.weatherinstabug.domain.model.Hour
import com.example.weatherinstabug.domain.model.Weather
import com.example.weatherinstabug.presentation.model.CurrentConditionsUi
import com.example.weatherinstabug.presentation.model.DayUi
import com.example.weatherinstabug.presentation.model.HourUi
import com.example.weatherinstabug.presentation.model.WeatherUi

object WeatherMapper {
    fun responseToDomain(response: WeatherResponseDto): Weather {
        return Weather(
            latitude = response.latitude,
            longitude = response.longitude,
            timezone = response.timezone,
            resolvedAddress = response.resolvedAddress,
            description = response.description,
            tzOffset = response.tzoffset,
            currentConditions = CurrentConditions(
                dateTime = response.currentConditionsDto.datetime,
                temp = response.currentConditionsDto.temp,
                feelsLike = response.currentConditionsDto.feelslike,
                humidity = response.currentConditionsDto.humidity,
                dew = response.currentConditionsDto.dew,
                uvIndex = response.currentConditionsDto.uvindex,
                pressure = response.currentConditionsDto.pressure,
                windSpeed = response.currentConditionsDto.windspeed,
                windDir = response.currentConditionsDto.winddir,
                conditions = response.currentConditionsDto.conditions,
                icon = response.currentConditionsDto.icon,
                sunrise = response.currentConditionsDto.sunrise,
                sunset = response.currentConditionsDto.sunset,
                precipProb = response.currentConditionsDto.precipprob
            ),
            days = response.dayDtos.map { day ->
                Day(
                    dateTime = day.datetime,
                    tempMax = day.tempmax,
                    tempMin = day.tempmin,
                    temp = day.temp,
                    conditions = day.conditions,
                    icon = day.icon,
                    precipProb = day.precipprob,
                    windSpeed = day.windspeed,
                    windDir = day.winddir,
                    hours = day.hourDtos.map { hour ->
                        Hour(
                            dateTime = hour.datetime,
                            temp = hour.temp,
                            windSpeed = hour.windspeed,
                            icon = hour.icon,
                            precipProb = hour.precipprob
                        )
                    }
                )
            }
        )
    }

    fun domainToUi(weather: Weather): WeatherUi {
        return WeatherUi(
            latitude = weather.latitude,
            longitude = weather.longitude,
            timezone = weather.timezone,
            resolvedAddress = weather.resolvedAddress,
            description = weather.description,
            tzOffset = weather.tzOffset,
            currentConditions = CurrentConditionsUi(
                dateTime = weather.currentConditions.dateTime,
                temp = weather.currentConditions.temp,
                feelsLike = weather.currentConditions.feelsLike,
                humidity = weather.currentConditions.humidity,
                dew = weather.currentConditions.dew,
                uvIndex = weather.currentConditions.uvIndex,
                pressure = weather.currentConditions.pressure,
                windSpeed = weather.currentConditions.windSpeed,
                windDir = weather.currentConditions.windDir,
                conditions = weather.currentConditions.conditions,
                icon = weather.currentConditions.icon,
                sunrise = weather.currentConditions.sunrise,
                sunset = weather.currentConditions.sunset,
                precipProb = weather.currentConditions.precipProb
            ),
            days = weather.days.map { day ->
                DayUi(
                    dateTime = day.dateTime,
                    tempMax = day.tempMax,
                    tempMin = day.tempMin,
                    temp = day.temp,
                    conditions = day.conditions,
                    icon = day.icon,
                    precipProb = day.precipProb,
                    windSpeed = day.windSpeed,
                    windDir = day.windDir,
                    hours = day.hours.map { hour ->
                        HourUi(
                            dateTime = hour.dateTime,
                            temp = hour.temp,
                            windSpeed = hour.windSpeed,
                            icon = hour.icon,
                            precipProb = hour.precipProb
                        )
                    }
                )
            }
        )
    }

    fun uiToDomain(weatherUi: WeatherUi): Weather {
        return Weather(
            latitude = weatherUi.latitude,
            longitude = weatherUi.longitude,
            timezone = weatherUi.timezone,
            resolvedAddress = weatherUi.resolvedAddress,
            description = weatherUi.description,
            tzOffset = weatherUi.tzOffset,
            currentConditions = CurrentConditions(
                dateTime = weatherUi.currentConditions.dateTime,
                temp = weatherUi.currentConditions.temp,
                feelsLike = weatherUi.currentConditions.feelsLike,
                humidity = weatherUi.currentConditions.humidity,
                dew = weatherUi.currentConditions.dew,
                uvIndex = weatherUi.currentConditions.uvIndex,
                pressure = weatherUi.currentConditions.pressure,
                windSpeed = weatherUi.currentConditions.windSpeed,
                windDir = weatherUi.currentConditions.windDir,
                conditions = weatherUi.currentConditions.conditions,
                icon = weatherUi.currentConditions.icon,
                sunrise = weatherUi.currentConditions.sunrise,
                sunset = weatherUi.currentConditions.sunset,
                precipProb = weatherUi.currentConditions.precipProb
            ),
            days = weatherUi.days.map { day ->
                Day(
                    dateTime = day.dateTime,
                    tempMax = day.tempMax,
                    tempMin = day.tempMin,
                    temp = day.temp,
                    conditions = day.conditions,
                    icon = day.icon,
                    precipProb = day.precipProb,
                    windSpeed = day.windSpeed,
                    windDir = day.windDir,
                    hours = day.hours.map { hour ->
                        Hour(
                            dateTime = hour.dateTime,
                            temp = hour.temp,
                            windSpeed = hour.windSpeed,
                            icon = hour.icon,
                            precipProb = hour.precipProb
                        )
                    }
                )
            }
        )
    }
}