package de.app.ui.geo.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mapbox.mapboxsdk.geometry.LatLng
import dagger.hilt.android.AndroidEntryPoint
import de.app.databinding.FragmentGeoDataTabFilterBinding
import de.app.ui.components.SimpleFragment
import de.app.ui.geo.GeoDataViewModel
import de.app.ui.geo.MapObjectCategory
import javax.inject.Inject

@AndroidEntryPoint
class GeoDataFilterFragment :SimpleFragment<FragmentGeoDataTabFilterBinding>() {

    @Inject
    lateinit var viewModel: GeoDataViewModel

    lateinit var catAdapter: CategoriesWrapperAdapter
    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentGeoDataTabFilterBinding.inflate(inflater, container, false)

    override fun setup() {
        val categories = setupCategories()

        catAdapter = CategoriesWrapperAdapter(requireContext(), categories)

        binding.categoriesView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            viewModel.currentTab.value = 1
            viewModel.objectCategory.value = MapObjectCategory(category = categories[groupPosition].second[childPosition],
                listOf(LatLng(51.3663, 11.9817)))
            true
        }

        viewModel.currentTab.observe(viewLifecycleOwner) {
            catAdapter.notifyDataSetInvalidated()
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