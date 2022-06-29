package de.app.ui.geo.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mapbox.mapboxsdk.geometry.LatLng
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.geo.GeodataService
import de.app.databinding.FragmentGeoDataTabFilterBinding
import de.app.ui.components.SimpleFragment
import de.app.ui.geo.GeoDataViewModel
import de.app.ui.geo.GeoObjectSet
import de.app.ui.util.toast
import javax.inject.Inject

@AndroidEntryPoint
class GeoDataFilterFragment :SimpleFragment<FragmentGeoDataTabFilterBinding>() {

    @Inject
    lateinit var viewModel: GeoDataViewModel

    @Inject
    lateinit var service: GeodataService

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentGeoDataTabFilterBinding.inflate(inflater, container, false)

    override fun setup() {
        val categories = service.getAllCategories()
        val catAdapter = CategoriesWrapperAdapter(requireContext(), categories)

        binding.categoriesView.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            val set = categories[groupPosition].sets[childPosition]
            service.getSetById(set.id).onSuccess {
                viewModel.objectSet.value = GeoObjectSet(name = it.name, it.positions)
            }.onFailure {
                requireActivity().toast("Failed to display category: ${it.message}")
            }
            viewModel.tabRequested.value = 1
            true
        }

        viewModel.tabState.observe(viewLifecycleOwner) {
            if (it == 0) catAdapter.notifyDataSetInvalidated()
        }

        binding.categoriesView.setAdapter(catAdapter)
    }
}