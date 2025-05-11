package com.example.weatherinstabug.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission

@SuppressLint("MissingPermission")
class LocationUtils(private val context: Context, private val locationCallback: LocationCallback) : LocationListener {

    private lateinit var locationManager: LocationManager
    private var coordinates: Pair<Double, Double>? = null
    private var hasReportedError = false

    init {
        try {
            getCurrentLocation()
        } catch (e: Exception) {
            Log.e("LocationUtils", "Error in initialization: ${e.message}")
            reportError("Location services initialization error")
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getCurrentLocation() {
        try {
            locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager

            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

//            removed as you asked to use GPS
//            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGpsEnabled) {
                Log.e("LocationUtils", "No location providers enabled")
                reportError("Please enable location services in your device settings")
                return
            }

            val lastKnownLocationGPS = if (isGpsEnabled) {
                try {
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                } catch (e: Exception) {
                    Log.e("LocationUtils", "Error getting GPS last location: ${e.message}")
                    null
                }
            } else null
//
//            val lastKnownLocationNetwork = if (isNetworkEnabled) {
//                try {
//                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//                } catch (e: Exception) {
//                    Log.e("LocationUtils", "Error getting Network last location: ${e.message}")
//                    null
//                }
//            } else null

            val lastKnownLocation = when {
                lastKnownLocationGPS != null -> lastKnownLocationGPS
//                lastKnownLocationNetwork != null -> lastKnownLocationNetwork
                else -> null
            }

            if (lastKnownLocation != null) {
                Log.d("LocationUtils", "Using last known location: ${lastKnownLocation.latitude}, ${lastKnownLocation.longitude}")
                coordinates = Pair(lastKnownLocation.latitude, lastKnownLocation.longitude)
                locationCallback.onLocationReceived(coordinates!!)
                return
            }

            var requestedUpdates = false

            if (isGpsEnabled) {
                try {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000,
                        0f,
                        this
                    )
                    requestedUpdates = true
                    Log.d("LocationUtils", "Requested GPS updates")
                } catch (e: Exception) {
                    Log.e("LocationUtils", "Failed to request GPS updates: ${e.message}")
                }
            }

//            if (isNetworkEnabled) {
//                try {
//                    locationManager.requestLocationUpdates(
//                        LocationManager.NETWORK_PROVIDER,
//                        5000,
//                        5f,
//                        this
//                    )
//                    requestedUpdates = true
//                    Log.d("LocationUtils", "Requested Network updates")
//                } catch (e: Exception) {
//                    Log.e("LocationUtils", "Failed to request Network updates: ${e.message}")
//                }
//            }

            if (!requestedUpdates) {
                reportError("Unable to request location updates")
            }

            Handler(Looper.getMainLooper()).postDelayed({
                if (coordinates == null) {
                    reportError("Location timeout - could not determine your location")
                    removeLocationUpdates()
                }
            }, 45000)

        } catch (e: Exception) {
            Log.e("LocationUtils", "Error in getCurrentLocation: ${e.message}")
            reportError("Location error: ${e.message}")
        }
    }

    /**
     *  `removeUpdates` to save resources after first location received.
     */
    override fun onLocationChanged(location: Location) {
        Log.d("LocationUtils", "Location received: ${location.latitude}, ${location.longitude}")
        coordinates = Pair(location.latitude, location.longitude)
        locationCallback.onLocationReceived(coordinates!!)
        removeLocationUpdates()
    }

    private fun removeLocationUpdates() {
        try {
            locationManager.removeUpdates(this)
            Log.d("LocationUtils", "Location updates removed")
        } catch (e: Exception) {
            Log.e("LocationUtils", "Error removing location updates: ${e.message}")
        }
    }

    private fun reportError(message: String) {
        if (!hasReportedError) {
            hasReportedError = true
            Log.e("LocationUtils", "Error: $message")
            locationCallback.onLocationError(message)
        }
    }

    interface LocationCallback {
        fun onLocationReceived(coordinates: Pair<Double, Double>)
        fun onLocationError(errorMessage: String)
    }
}