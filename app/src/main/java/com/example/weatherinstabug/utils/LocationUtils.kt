package com.example.weatherinstabug.utils

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.annotation.RequiresPermission

class LocationUtils(private val context: Context) : LocationListener {

    lateinit var locationManager: LocationManager

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getCurrentLocation() {
        locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        try {
            if (isGpsAvailable())
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
            else
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *  `removeUpdates` to save resources after first location received.
     */
    override fun onLocationChanged(location: Location) {
        locationManager.removeUpdates { this }
    }

    fun isGpsAvailable() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)


    fun isNetworkAvailable() = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

}