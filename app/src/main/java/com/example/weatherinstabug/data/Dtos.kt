package com.example.weatherinstabug.data

import android.os.Parcel
import android.os.Parcelable

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val resolvedAddress: String,
    val description: String,
    val currentConditions: CurrentConditions,
    val days: List<Day>
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
    }

    constructor(parcel: Parcel) : this(
        latitude = parcel.readDouble(),
        longitude = parcel.readDouble(),
        timezone = parcel.readString() ?: "",
        resolvedAddress = parcel.readString() ?: "",
        description = parcel.readString() ?: "",
        currentConditions = parcel.readParcelable(CurrentConditions::class.java.classLoader) ?: CurrentConditions("", 0.0, 0.0, 0.0, 0.0, 0, 0.0, 0.0, 0.0, "", "", "", "", 0.0),
        days = mutableListOf<Day>().apply {
            parcel.readTypedList(this, Day.CREATOR)
        }
    )

    companion object CREATOR : Parcelable.Creator<WeatherResponse> {
        override fun createFromParcel(parcel: Parcel): WeatherResponse {
            return WeatherResponse(parcel)
        }

        override fun newArray(size: Int): Array<WeatherResponse?> {
            return arrayOfNulls(size)
        }
    }
}

data class CurrentConditions(
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
) : Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(datetime)
        dest.writeDouble(temp)
        dest.writeDouble(feelslike)
        dest.writeDouble(humidity)
        dest.writeDouble(dew)
        dest.writeInt(uvindex)
        dest.writeDouble(pressure)
        dest.writeDouble(windspeed)
        dest.writeDouble(winddir)
        dest.writeString(conditions)
        dest.writeString(icon)
        dest.writeString(sunrise)
        dest.writeString(sunset)
        dest.writeDouble(precipprob)
    }

    constructor(parcel: Parcel) : this(
        datetime = parcel.readString() ?: "",
        temp = parcel.readDouble(),
        feelslike = parcel.readDouble(),
        humidity = parcel.readDouble(),
        dew = parcel.readDouble(),
        uvindex = parcel.readInt(),
        pressure = parcel.readDouble(),
        windspeed = parcel.readDouble(),
        winddir = parcel.readDouble(),
        conditions = parcel.readString() ?: "",
        icon = parcel.readString() ?: "",
        sunrise = parcel.readString() ?: "",
        sunset = parcel.readString() ?: "",
        precipprob = parcel.readDouble()
    )

    companion object CREATOR : Parcelable.Creator<CurrentConditions> {
        override fun createFromParcel(parcel: Parcel): CurrentConditions {
            return CurrentConditions(parcel)
        }

        override fun newArray(size: Int): Array<CurrentConditions?> {
            return arrayOfNulls(size)
        }
    }
}

data class Day(
    val datetime: String,
    val tempmax: Double,
    val tempmin: Double,
    val temp: Double,
    val conditions: String,
    val icon: String,
    val precipprob: Double,
    val windspeed: Double,
    val winddir: String,
    val hours: List<Hour>
) : Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(datetime)
        dest.writeDouble(tempmax)
        dest.writeDouble(tempmin)
        dest.writeString(conditions)
        dest.writeString(icon)
        dest.writeDouble(precipprob)
        dest.writeDouble(windspeed)
        dest.writeString(winddir)
        dest.writeTypedList(hours)
        dest.writeDouble(temp)
    }

    constructor(parcel: Parcel) : this(
        datetime = parcel.readString() ?: "",
        tempmax = parcel.readDouble(),
        tempmin = parcel.readDouble(),
        temp = parcel.readDouble(),
        conditions = parcel.readString() ?: "",
        icon = parcel.readString() ?: "",
        precipprob = parcel.readDouble(),
        windspeed = parcel.readDouble(),
        winddir = parcel.readString() ?: "",
        hours = mutableListOf<Hour>().apply {
            parcel.readTypedList(this, Hour.CREATOR)
        }
    )

    companion object CREATOR : Parcelable.Creator<Day> {
        override fun createFromParcel(parcel: Parcel): Day {
            return Day(parcel)
        }

        override fun newArray(size: Int): Array<Day?> {
            return arrayOfNulls(size)
        }
    }
}

data class Hour(
    val datetime: String,
    val temp: Double,
    val windspeed: Double,
    val icon: String,
    val precipprob: Double
) : Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(datetime)
        dest.writeDouble(temp)
        dest.writeDouble(windspeed)
        dest.writeString(icon)
        dest.writeDouble(precipprob)
    }

    constructor(parcel: Parcel) : this(
        datetime = parcel.readString() ?: "",
        temp = parcel.readDouble(),
        windspeed = parcel.readDouble(),
        icon = parcel.readString() ?: "",
        precipprob = parcel.readDouble()
    )

    companion object CREATOR : Parcelable.Creator<Hour> {
        override fun createFromParcel(parcel: Parcel): Hour {
            return Hour(parcel)
        }

        override fun newArray(size: Int): Array<Hour?> {
            return arrayOfNulls(size)
        }
    }
}