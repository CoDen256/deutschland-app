package de.app.ui.geo

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager
import com.mapbox.mapboxsdk.plugins.annotation.CircleOptions
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapbox.mapboxsdk.utils.ColorUtils
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.core.onSuccess
import de.app.databinding.FragmentGeoDataTabMapBinding
import de.app.geo.LocationRepository
import javax.inject.Inject


@AndroidEntryPoint
class GeoDataMapFragment(private val data: LiveData<MapObjectInfo>) : Fragment() {

    private var style = "basic"
    private val apiKey: String
        get() = getMapTilerKey()
    private val styleUrl: String
        get() = "https://api.maptiler.com/maps/$style/style.json?key=$apiKey"


    @Inject
    lateinit var repo: LocationRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGeoDataTabMapBinding.inflate(inflater, container, false)

        setupMap(binding.mapView, savedInstanceState)

        data.observe(viewLifecycleOwner) {
            binding.currentObject.text = getString(R.string.selected_object, it.name)
        }

        return binding.root
    }

    private fun setupMap(binding: FragmentGeoDataTabMapBinding, savedInstanceState: Bundle?) {
        binding.mapView.onCreate(savedInstanceState)

        repo.requestAddress(requireContext()).onSuccess { result ->
            result.onSuccess {
                binding.currentPosition.text =
                    getString(R.string.your_position_is, it.getAddressLine(0))

            }
        }

        mapView.getMapAsync { map ->
            // Set the style after mapView was loaded
            map.setStyle(styleUrl) {
                map.uiSettings.isAttributionEnabled = false
                val circleManager = CircleManager(mapView, map, it)
                val circleOptions = CircleOptions()
                    .withLatLng(LatLng(51.3563, 11.9917))
                    .withCircleColor(ColorUtils.colorToRgbaString(Color.RED))
                    .withCircleRadius(5f)
                circleManager.create(circleOptions)

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