package de.app.ui

import android.R
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

    private var style = "basic"
    private val apiKey: String
        get() = getMapTilerKey()
    private val styleUrl : String
       get() = "https://api.maptiler.com/maps/$style/style.json?key=$apiKey"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, null)

        binding = ActivityGeoDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCategories()
        setupMap(savedInstanceState)
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        val mapView: MapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            // Set the style after mapView was loaded
            map.setStyle(styleUrl) {
                map.uiSettings.isAttributionEnabled = false
                // Set the map view center
                map.cameraPosition = CameraPosition.Builder()
                    .target(LatLng(51.3563, 11.9917))
                    .zoom(14.0)
                    .build()
            }
        }
    }

    private fun setupCategories() {
        val items = arrayOf("Shutzgebiete", "Umwelt", "Landwirtschaft")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, items)
        binding.dropdown.setAdapter(adapter)
    }

    private fun getMapTilerKey(): String {
        return packageManager.getApplicationInfo(
            packageName,
            PackageManager.GET_META_DATA
        ).metaData.getString("com.maptiler.simplemap.mapTilerKey")!!
    }
}