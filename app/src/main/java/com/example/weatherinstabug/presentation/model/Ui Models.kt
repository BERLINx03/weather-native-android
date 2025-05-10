package com.example.weatherinstabug.presentation.model

import android.os.Parcel
import android.os.Parcelable

data class WeatherUi(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val resolvedAddress: String,
    val description: String,
    val currentConditions: CurrentConditionsUi,
    val tzOffset: Int,
    val days: List<DayUi>
): Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeDouble(latitude)
        dest.writeDouble(longitude)
        dest.writeString(timezone)
        dest.writeString(resolvedAddress)
        dest.writeString(description)
        dest.writeParcelable(currentConditions, flags)
        dest.writeTypedList(days)
        dest.writeInt(tzOffset)
    }

    constructor(parcel: Parcel) : this(
        latitude = parcel.readDouble(),
        longitude = parcel.readDouble(),
        timezone = parcel.readString() ?: "",
        resolvedAddress = parcel.readString() ?: "",
        description = parcel.readString() ?: "",
        tzOffset = parcel.readInt(),
        currentConditions = parcel.readParcelable(CurrentConditionsUi::class.java.classLoader)
            ?: CurrentConditionsUi("", 0.0, 0.0, 0.0, 0.0, 0, 0.0, 0.0, 0.0, "", "", "", "", 0.0),
        days = mutableListOf<DayUi>().apply {
            parcel.readTypedList(this, DayUi.CREATOR)
        }
    )

    companion object CREATOR : Parcelable.Creator<WeatherUi> {
        override fun createFromParcel(parcel: Parcel): WeatherUi {
            return WeatherUi(parcel)
        }

        override fun newArray(size: Int): Array<WeatherUi?> {
            return arrayOfNulls(size)
        }
    }
}

data class CurrentConditionsUi(
    val dateTime: String,
    val temp: Double,
    val feelsLike: Double,
    val humidity: Double,
    val dew: Double,
    val uvIndex: Int,
    val pressure: Double,
    val windSpeed: Double,
    val windDir: Double,
    val conditions: String,
    val icon: String,
    val sunrise: String,
    val sunset: String,
    val precipProb: Double
) : Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(dateTime)
        dest.writeDouble(temp)
        dest.writeDouble(feelsLike)
        dest.writeDouble(humidity)
        dest.writeDouble(dew)
        dest.writeInt(uvIndex)
        dest.writeDouble(pressure)
        dest.writeDouble(windSpeed)
        dest.writeDouble(windDir)
        dest.writeString(conditions)
        dest.writeString(icon)
        dest.writeString(sunrise)
        dest.writeString(sunset)
        dest.writeDouble(precipProb)
    }

    constructor(parcel: Parcel) : this(
        dateTime = parcel.readString() ?: "",
        temp = parcel.readDouble(),
        feelsLike = parcel.readDouble(),
        humidity = parcel.readDouble(),
        dew = parcel.readDouble(),
        uvIndex = parcel.readInt(),
        pressure = parcel.readDouble(),
        windSpeed = parcel.readDouble(),
        windDir = parcel.readDouble(),
        conditions = parcel.readString() ?: "",
        icon = parcel.readString() ?: "",
        sunrise = parcel.readString() ?: "",
        sunset = parcel.readString() ?: "",
        precipProb = parcel.readDouble()
    )

    companion object CREATOR : Parcelable.Creator<CurrentConditionsUi> {
        override fun createFromParcel(parcel: Parcel): CurrentConditionsUi {
            return CurrentConditionsUi(parcel)
        }

        override fun newArray(size: Int): Array<CurrentConditionsUi?> {
            return arrayOfNulls(size)
        }
    }
}

data class DayUi(
    val dateTime: String,
    val tempMax: Double,
    val tempMin: Double,
    val temp: Double,
    val conditions: String,
    val icon: String,
    val precipProb: Double,
    val windSpeed: Double,
    val windDir: String,
    val hours: List<HourUi>
) : Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(dateTime)
        dest.writeDouble(tempMax)
        dest.writeDouble(tempMin)
        dest.writeString(conditions)
        dest.writeString(icon)
        dest.writeDouble(precipProb)
        dest.writeDouble(windSpeed)
        dest.writeString(windDir)
        dest.writeTypedList(hours)
        dest.writeDouble(temp)
    }

    constructor(parcel: Parcel) : this(
        dateTime = parcel.readString() ?: "",
        tempMax = parcel.readDouble(),
        tempMin = parcel.readDouble(),
        temp = parcel.readDouble(),
        conditions = parcel.readString() ?: "",
        icon = parcel.readString() ?: "",
        precipProb = parcel.readDouble(),
        windSpeed = parcel.readDouble(),
        windDir = parcel.readString() ?: "",
        hours = mutableListOf<HourUi>().apply {
            parcel.readTypedList(this, HourUi.CREATOR)
        }
    )

    companion object CREATOR : Parcelable.Creator<DayUi> {
        override fun createFromParcel(parcel: Parcel): DayUi {
            return DayUi(parcel)
        }

        override fun newArray(size: Int): Array<DayUi?> {
            return arrayOfNulls(size)
        }
    }
}

data class HourUi(
    val dateTime: String,
    val temp: Double,
    val windSpeed: Double,
    val icon: String,
    val precipProb: Double
) : Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(dateTime)
        dest.writeDouble(temp)
        dest.writeDouble(windSpeed)
        dest.writeString(icon)
        dest.writeDouble(precipProb)
    }

    constructor(parcel: Parcel) : this(
        dateTime = parcel.readString() ?: "",
        temp = parcel.readDouble(),
        windSpeed = parcel.readDouble(),
        icon = parcel.readString() ?: "",
        precipProb = parcel.readDouble()
    )

    companion object CREATOR : Parcelable.Creator<HourUi> {
        override fun createFromParcel(parcel: Parcel): HourUi {
            return HourUi(parcel)
        }

        override fun newArray(size: Int): Array<HourUi?> {
            return arrayOfNulls(size)
        }
    }
}