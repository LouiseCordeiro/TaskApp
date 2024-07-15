package com.example.todoapp.data

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient

class LocationHelper(private val fusedLocationClient: FusedLocationProviderClient) {

    @SuppressLint("MissingPermission")
    fun getLastLocation(onLocationReceived: (Location?) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                onLocationReceived(location)
            }
    }
}