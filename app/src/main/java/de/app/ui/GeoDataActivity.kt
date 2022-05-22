package de.app.ui

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView

import de.app.databinding.ActivityGeoDataBinding

class GeoDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGeoDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mapTilerKey = getMapTilerKey()
//        validateKey(mapTilerKey)
        val styleUrl = "https://api.maptiler.com/maps/streets/style.json?key=${mapTilerKey}";
        // Get the MapBox context
        Mapbox.getInstance(this, null)


        binding = ActivityGeoDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = arrayOf("Shutzgebiete", "Umwelt", "Landwirtschaft")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        binding.dropdown.setAdapter(adapter)


        val mapView: MapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            // Set the style after mapView was loaded
            map.setStyle(styleUrl) {
                map.uiSettings.setAttributionMargins(15, 0, 0, 15)
                // Set the map view center
                map.cameraPosition = CameraPosition.Builder()
                    .target(LatLng(47.127757, 8.579139))
                    .zoom(10.0)
                    .build()
            }
        }
    }

    private fun getMapTilerKey(): String? {
        return packageManager.getApplicationInfo(
            packageName,
            PackageManager.GET_META_DATA
        ).metaData.getString("com.maptiler.simplemap.mapTilerKey")
    }
}