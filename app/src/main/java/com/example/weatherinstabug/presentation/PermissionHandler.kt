package com.example.weatherinstabug.presentation

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference

class PermissionsHandler(activity: Activity) {

    companion object {
        val WEATHER_APP_PERMISSIONS = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE
        )
    }
    private val activityRef = WeakReference(activity)

    interface PermissionCallback {
        fun onPermissionResult(granted: Boolean)
    }

    fun hasPermissions(permissions: Array<String>): Boolean {
        val activity = activityRef.get() ?: return false

        return permissions.all {
            ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermissions(permissions: Array<String>, requestCode: Int) {
        val activity = activityRef.get() ?: return
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }

    fun handlePermissionResult(
        grantResults: IntArray,
        callback: PermissionCallback
    ) {
        if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            callback.onPermissionResult(true)
        } else {
            callback.onPermissionResult(false)
        }
    }

    fun clear() {
        activityRef.clear()
    }
}