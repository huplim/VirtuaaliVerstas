package com.example.virtuaaliverstas

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.location.Location
import android.location.LocationManager.FUSED_PROVIDER
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationManager.NETWORK_PROVIDER
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow


@SuppressLint("InlinedApi")
class LocationViewModel(application: Application) : AndroidViewModel(application),
    LocationListener {
    private val gpsCoordinates = Pair(0.0, 0.0)
    val locationData = MutableStateFlow(gpsCoordinates)

    private val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager


    init {
        try {
            // Get last known location from a working provider
            val lastKnownLocation: Location? = locationManager.getLastKnownLocation(FUSED_PROVIDER)
                ?: locationManager.getLastKnownLocation(NETWORK_PROVIDER)
                ?: locationManager.getLastKnownLocation(GPS_PROVIDER)
            // Update location if it exists
            lastKnownLocation?.let {
                locationData.value = Pair(it.latitude, it.longitude)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onLocationChanged(p0: Location) {
        locationData.value = Pair(p0.latitude, p0.longitude)
    }
}