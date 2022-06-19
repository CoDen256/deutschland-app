package de.app.geo

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LastLocationRequest
import com.google.android.gms.tasks.Task
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {
    @SuppressLint("MissingPermission") // Only called when holding location permission.
    fun requestLocation(): Task<Location> {
        return fusedLocationProviderClient.getLastLocation(LastLocationRequest.Builder().build())
    }
}