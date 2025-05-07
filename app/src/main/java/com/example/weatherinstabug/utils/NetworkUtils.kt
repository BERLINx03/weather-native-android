package com.example.weatherinstabug.utils


import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkState(private val connectivityManager: ConnectivityManager) {

    fun hasValidInternet (): Boolean {
        val activeNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    }
}