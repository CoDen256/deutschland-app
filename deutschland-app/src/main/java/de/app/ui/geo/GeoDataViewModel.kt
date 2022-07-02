package de.app.ui.geo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapbox.mapboxsdk.geometry.LatLng
import de.app.core.onSuccess
import de.app.core.LocationRepository
import de.app.ui.util.geoDecode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeoDataViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {

    val objectSet = MutableLiveData<GeoObjectSet>()
    val currentPosition = MutableLiveData<CurrentPositionGeoObject>()
    val tabState = MutableLiveData<Int>()
    val tabRequested = MutableLiveData<Int>()

    fun init(context: Context) {
        repository.requestLocation().onSuccess { location ->
            context.geoDecode(location).map { it.first() }.onSuccess {
                currentPosition.value = CurrentPositionGeoObject(
                    location = LatLng(location.latitude, location.longitude),
                    address = it.getAddressLine(0)
                )
            }
        }
    }
}


data class CurrentPositionGeoObject(
    val location: LatLng,
    val address: String,
)

data class GeoObjectSet(
    val name: String,
    val objects: List<LatLng>
)