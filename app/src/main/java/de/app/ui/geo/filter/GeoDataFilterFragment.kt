package de.app.ui.geo.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mapbox.mapboxsdk.geometry.LatLng
import dagger.hilt.android.AndroidEntryPoint
import de.app.databinding.FragmentGeoDataTabFilterBinding
import de.app.ui.components.SimpleFragment
import de.app.ui.geo.GeoDataViewModel
import de.app.ui.geo.MapObject

@AndroidEntryPoint
class GeoDataFilterFragment() :SimpleFragment<FragmentGeoDataTabFilterBinding>() {

    private val viewModel: GeoDataViewModel by viewModels({requireParentFragment()})

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentGeoDataTabFilterBinding.inflate(inflater, container, false)

    override fun setup() {
        val categories = setupCategories()

        val catAdapter = CategoriesWrapperAdapter(requireContext(), categories)

//        fragmentCollection.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageScrollStateChanged(state: Int) {
//                catAdapter.notifyDataSetInvalidated()
//            }
//        })

        binding.categoriesView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
//            fragmentCollection.moveToMap(MapObjectInfo(categories[groupPosition].second[childPosition]))
            viewModel.objects.value = listOf(MapObject(LatLng(51.3663, 11.9817)))
            viewModel.category.value = categories[groupPosition].second[childPosition]
            true
        }

        binding.categoriesView.setAdapter(catAdapter)
    }

    private fun setupCategories(): List<Pair<String, List<String>>> {
        val listDetail = HashMap<String, List<String>>()

        val category0 = listOf(
            "Energie", "Schutzgebiete", "Wasser", "Boden"
        )

        val category1 = listOf(
            "Acker und Wald-Boden", "Landwirtschaft", "Forstwirtschaft"
        )

        val category2 = listOf(
            "Strasse", "Schiene", "Flug", "Schiff"
        )
        listDetail.put("Umwelt und Energie", category0)
        listDetail.put("Land und Forstwirtschaft", category1)
        listDetail.put("Verkehr und Technologie", category2)
        return listDetail.entries.map { it.toPair() }
    }
}