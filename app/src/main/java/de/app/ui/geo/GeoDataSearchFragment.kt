package de.app.ui.geo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
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

        fragmentCollection.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrollStateChanged(state: Int) {
                catAdapter.notifyDataSetInvalidated()
            }
        })

        categoriesView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            fragmentCollection.moveToMap(MapObjectInfo(categories[groupPosition].second[childPosition]))
            true
        }


        categoriesView.setAdapter(catAdapter)
    }.root

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