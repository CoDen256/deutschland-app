package de.app.ui.geo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.app.databinding.FragmentGeoDataTabSearchBinding

class GeoDataSearchFragment(
    private val fragmentCollection: GeoDataFragmentCollection,
) :Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentGeoDataTabSearchBinding.inflate(inflater, container, false).apply {

        val categories = setupCategories()

        val catAdapter= CategoriesWrapperAdapter(requireContext(), categories)

        categoriesView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            fragmentCollection.moveToMap(MapObjectInfo(categories[groupPosition].second[childPosition]))
            true
        }

        categoriesView.setAdapter(catAdapter)
    }.root

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
}