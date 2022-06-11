package de.app.ui.geo

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import de.app.databinding.FragmentGeoDataBinding

class GeoDataFragment : Fragment() {

    private var style = "basic"
    private val apiKey: String
        get() = getMapTilerKey()
    private val styleUrl: String
        get() = "https://api.maptiler.com/maps/$style/style.json?key=$apiKey"

    private lateinit var viewModel: GeoDataViewModel
    private lateinit var binding: FragmentGeoDataBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Mapbox.getInstance(requireActivity(), null)
        binding = FragmentGeoDataBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[GeoDataViewModel::class.java]

        val cats = setupCategories()

        val catAdapter= CategoriesAdapter(requireContext(), cats)

        binding.expandableListView.apply {
            setAdapter(catAdapter)
        }

        setupMap(savedInstanceState)

        return binding.root
    }


    private fun setupCategories(): List<Pair<String, List<String>>> {
        val listDetail = HashMap<String, List<String>>()

        val category0 = listOf(
            "One", "Two", "Three", "Four"
        )

        val category1 = listOf(
            "One", "Two", "Three", "Four"
        )

        val category2 = listOf(
            "One", "Two", "Three", "Four"
        )
        listDetail.put("One", category0)
        listDetail.put("Two", category1)
        listDetail.put("Three", category2)
        return listDetail.entries.map { it.toPair() }
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

    private fun getMapTilerKey(): String = requireActivity().let {
        it.packageManager.getApplicationInfo(
            it.packageName,
            PackageManager.GET_META_DATA
        ).metaData.getString("com.maptiler.simplemap.mapTilerKey")!!
    }

}