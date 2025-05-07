package com.example.weatherinstabug.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.annotation.RequiresPermission

@SuppressLint("MissingPermission")
class LocationUtils(private val context: Context, private val locationCallback: LocationCallback) : LocationListener {

    lateinit var locationManager: LocationManager
    lateinit var coordinates: Pair<Double, Double>

    init {
        getCurrentLocation()
    }
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getCurrentLocation() {
        locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        val lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val lastKnownLocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        val lastKnownLocation = when {
            lastKnownLocationGPS != null -> lastKnownLocationGPS
            lastKnownLocationNetwork != null -> lastKnownLocationNetwork
            else -> null
        }

        if (lastKnownLocation != null) {
            Log.d("Weather", "Using last known location: ${lastKnownLocation.latitude} ${lastKnownLocation.longitude}")
            coordinates = Pair(lastKnownLocation.latitude, lastKnownLocation.longitude)
            locationCallback.onLocationReceived(coordinates)
            return
        }
        try {
            if (isGpsAvailable()) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
                Log.d("Weather"," done with gps")
            }

            else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
                Log.d("Weather","done with network")
            }
        } catch (e: Exception) {
            Log.d("Weather"," problem ${e.localizedMessage} ")
        }
    }

    /**
     *  `removeUpdates` to save resources after first location received.
     */
    override fun onLocationChanged(location: Location) {
        Log.d("Weather","${location.latitude} ${location.longitude}")
        coordinates = Pair(location.latitude,location.longitude)
        locationCallback.onLocationReceived(coordinates)
        locationManager.removeUpdates { this }
    }

    fun isGpsAvailable() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    interface LocationCallback{
        fun onLocationReceived(coordinates: Pair<Double, Double>)
    }
}