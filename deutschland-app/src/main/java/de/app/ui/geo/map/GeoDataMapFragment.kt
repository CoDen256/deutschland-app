package de.app.ui.geo.map

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager
import com.mapbox.mapboxsdk.plugins.annotation.CircleOptions
import com.mapbox.mapboxsdk.utils.ColorUtils
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.databinding.FragmentGeoDataTabMapBinding
import de.app.ui.components.SimpleFragment
import de.app.ui.geo.GeoDataViewModel
import java.lang.Double.max
import javax.inject.Inject


@AndroidEntryPoint
class GeoDataMapFragment : SimpleFragment<FragmentGeoDataTabMapBinding>() {

    private val default = LatLng(51.3563, 11.9917)
    private val style = "basic"
    private val apiKey: String
        get() = getMapTilerKey()
    private val styleUrl: String
        get() = "https://api.maptiler.com/maps/$style/style.json?key=$apiKey"
    private val delta = 1

    @Inject
    lateinit var viewModel: GeoDataViewModel

    private var savedInstanceState: Bundle? = null

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentGeoDataTabMapBinding.inflate(inflater, container, false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        preInit(savedInstanceState)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun preInit(savedInstanceState: Bundle?) {
        Mapbox.getInstance(requireActivity(), null)
        this.savedInstanceState = savedInstanceState
    }

    override fun setup() {
        setupMap()

        viewModel.objectSet.observe(viewLifecycleOwner) {
            binding.currentObject.text = getString(R.string.selected_object, it.name)
            updateMap(viewModel.currentPosition.value?.location, it.objects)
        }

        viewModel.currentPosition.observe(viewLifecycleOwner) { currentPosition ->
            binding.currentPosition.text = getString(R.string.your_position_is, currentPosition.address)
            updateMap(currentPosition.location, viewModel.objectSet.value?.objects.orEmpty())
        }

    }

    private fun setupMap() {
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { map ->
            map.setStyle(styleUrl) {
                map.uiSettings.isAttributionEnabled = false
                computePositionAndSetCamera(
                    map,
                    viewModel.currentPosition.value?.location,
                    viewModel.objectSet.value?.objects.orEmpty()
                )
            }
        }
    }

    private fun updateMap(currentPosition: LatLng?, objects: List<LatLng>) {
        binding.mapView.getMapAsync { map ->
            map.setStyle(styleUrl) { style ->
                map.uiSettings.isAttributionEnabled = false
                renderPositions(currentPosition, map, style, objects)
                computePositionAndSetCamera(map, currentPosition, objects)
            }
        }
    }

    private fun renderPositions(currentPosition: LatLng?, map: MapboxMap, style: Style, objects: List<LatLng>) {
        val locations = ArrayList<CircleOptions>()
        currentPosition?.let {
            locations.add(createLocation(it, Color.BLUE))
        }
        objects.forEach {
            locations.add(createLocation(it, Color.MAGENTA))
        }
        displayLocations(locations, map, style)
    }

    private fun computePositionAndSetCamera(map: MapboxMap, currentPosition: LatLng?, objects: List<LatLng>) {
        val position = currentPosition ?: default

        var start = LatLngBounds.from(
            position.latitude+delta, position.longitude+delta,
            position.latitude-delta, position.longitude-delta
        )

        objects.forEach {
            start = start.include(it)
        }
        setCamera(map, start.center, 25-((start.latitudeSpan-0.2)/(6-0.2))*(20))
    }

    private fun setCamera(map: MapboxMap, location: LatLng, zoom: Double) {
        map.cameraPosition = CameraPosition.Builder()
            .target(LatLng(location.latitude, location.longitude))
            .zoom(zoom)
            .build()
    }

    private fun displayLocations(locations: List<CircleOptions>, map: MapboxMap, style: Style) {
        val circleManager = CircleManager(binding.mapView, map, style)
        circleManager.create(locations)
    }

    private fun createLocation(location: LatLng, color: Int): CircleOptions {
        return CircleOptions()
            .withLatLng(LatLng(location.latitude, location.longitude))
            .withCircleColor(ColorUtils.colorToRgbaString(color))
            .withCircleRadius(5f)
    }

    private fun getMapTilerKey(): String = requireActivity().let {
        it.packageManager.getApplicationInfo(
            it.packageName,
            PackageManager.GET_META_DATA
        ).metaData.getString("com.maptiler.simplemap.mapTilerKey")!!
    }
}