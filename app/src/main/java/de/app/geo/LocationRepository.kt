package de.app.geo

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LastLocationRequest
import com.google.android.gms.tasks.Task
import de.app.core.onSuccess
import de.app.data.model.Address
import de.app.data.model.simplify
import de.app.ui.util.geoDecode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {
    @SuppressLint("MissingPermission") // Only called when holding location permission.
    fun requestLocation(): Task<Location> {
        fusedLocationProviderClient.asGoogleApiClient()
        return fusedLocationProviderClient.getLastLocation(LastLocationRequest.Builder().build())
    }

    fun requestAddress(context: Context): Task<Result<android.location.Address>> {
        return requestLocation()
            .onSuccess { l ->
                val result = context.geoDecode(l)
                result.map { it.first() }
            }
    }

    fun requestSimplifiedAddress(context: Context): Task<Result<Address>> {
        return requestAddress(context).onSuccess { r -> r.map { it.simplify() } }
    }
}