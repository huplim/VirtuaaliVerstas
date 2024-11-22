package com.example.virtuaaliverstas

import android.app.Application
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow


class LocationViewModel(application: Application) : AndroidViewModel(application),
    LocationListener {
    private val gpsCoordinates = Pair(0.0, 0.0)
    val locationData = MutableStateFlow(gpsCoordinates)

    val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    init {
        startListeningLocationUpdates()
    }

    fun startListeningLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(
                // Update every 5 seconds
                LocationManager.FUSED_PROVIDER, 5000, 0f, this
            )
        }
        catch(e: SecurityException) {
            e.printStackTrace()
            locationData.value = Pair(0.0, 0.0)
        }

    }

    override fun onLocationChanged(p0: Location) {
        locationData.value = Pair(p0.latitude, p0.longitude)
    }
}