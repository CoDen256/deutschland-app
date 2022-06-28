package de.app.ui.geo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapbox.mapboxsdk.geometry.LatLng
import de.app.data.model.Address
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeoDataViewModel @Inject constructor(): ViewModel() {

    val objects = MutableLiveData<List<MapObject>>()
    val category = MutableLiveData<String>()
    val currentPosition = MutableLiveData<CurrentPositionObject>()
}


data class CurrentPositionObject(
    val location: LatLng,
    val address: Address,
)

data class MapObject(
    val location: LatLng
)