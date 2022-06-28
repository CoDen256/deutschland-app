package de.app.ui.geo.map

import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager
import com.mapbox.mapboxsdk.plugins.annotation.CircleOptions
import com.mapbox.mapboxsdk.utils.ColorUtils
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.api.geo.MapObjectInfo
import de.app.core.onSuccess
import de.app.databinding.FragmentGeoDataTabMapBinding
import de.app.geo.LocationRepository
import de.app.ui.components.SimpleFragment
import de.app.ui.util.geoDecode
import javax.inject.Inject


@AndroidEntryPoint
class GeoDataMapFragment(private val data: LiveData<MapObjectInfo>) : SimpleFragment<FragmentGeoDataTabMapBinding>() {

    private var style = "basic"
    private val apiKey: String
        get() = getMapTilerKey()
    private val styleUrl: String
        get() = "https://api.maptiler.com/maps/$style/style.json?key=$apiKey"

    @Inject
    lateinit var repo: LocationRepository
    private var savedInstanceState: Bundle? = null


    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentGeoDataTabMapBinding.inflate(inflater, container, false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance(requireActivity(), null)
        this.savedInstanceState = savedInstanceState
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setup() {
        setupMap()

        data.observe(viewLifecycleOwner) {
            binding.currentObject.text = getString(R.string.selected_object, it.name)
        }

        repo.requestLocation().onSuccess { location ->
            requireContext().geoDecode(location).map { it.first() }.onSuccess {
                binding.currentPosition.text =
                    getString(R.string.your_position_is, it.getAddressLine(0))
            }

            binding.mapView.getMapAsync { map ->
                map.setStyle(styleUrl) { style ->
                    map.uiSettings.isAttributionEnabled = false
                    displayLocation(binding, map, style, location, Color.RED)
                    setCamera(map, location, 14.0)
                }
            }
        }
    }

    private fun setCamera(map: MapboxMap, location: Location, zoom: Double) {
        map.cameraPosition = CameraPosition.Builder()
            .target(LatLng(location.latitude, location.longitude))
            .zoom(zoom)
            .build()
    }

    private fun displayLocation(
        binding: FragmentGeoDataTabMapBinding,
        map: MapboxMap,
        style: Style,
        location: Location,
        color: Int
    ) {
        val circleManager = CircleManager(binding.mapView, map, style)
        val circleOptions = CircleOptions()
            .withLatLng(LatLng(location.latitude, location.longitude))
            .withCircleColor(ColorUtils.colorToRgbaString(color))
            .withCircleRadius(5f)
        circleManager.create(circleOptions)
    }

    private fun setupMap() {
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { map ->
            // Set the style after mapView was loaded
            map.setStyle(styleUrl) { style->
                map.uiSettings.isAttributionEnabled = false

                // Set the map view center
                map.cameraPosition = CameraPosition.Builder()
                    .target(LatLng(51.3563, 11.9917))
                    .zoom(14.0)
                    .build()
            }
        }

    }

    private fun getMapTilerKey(): String = requireActivity().let {
        it.packageManager.getApplicationInfo(
            it.packageName,
            PackageManager.GET_META_DATA
        ).metaData.getString("com.maptiler.simplemap.mapTilerKey")!!
    }
}