package de.app.ui.geo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.mapbox.mapboxsdk.Mapbox
import de.app.R
import de.app.api.geo.MapObjectInfo
import de.app.databinding.FragmentGeoDataBinding
import de.app.ui.components.SimpleFragment
import de.app.ui.geo.filter.GeoDataFilterFragment
import de.app.ui.geo.map.GeoDataMapFragment

class GeoDataFragment : SimpleFragment<FragmentGeoDataBinding>() {

    private val tabToName by lazy {
        listOf(
            getString(R.string.geo_search_tab),
            getString(R.string.get_map_tab)
        )
    }

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentGeoDataBinding.inflate(inflater, container, false)

    override fun setup() {

        val geoDataPager = binding.geoDataPager

        geoDataPager.isUserInputEnabled = false
        geoDataPager.adapter = GeoDataFragmentCollection(this, geoDataPager)
        TabLayoutMediator(binding.tabLayout, geoDataPager) { tab, pos ->
            tab.text = tabToName[pos]
        }.attach()
    }
}

class GeoDataFragmentCollection(val fragment: Fragment, private val geoDataPager: ViewPager2) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> {
                GeoDataFilterFragment()
            }
            else -> {
                GeoDataMapFragment()
            }
        }

    fun moveToMap() {
        geoDataPager.setCurrentItem(1, true)
    }

    fun registerOnPageChangeCallback(onPageChangeCallback: ViewPager2.OnPageChangeCallback) {
        geoDataPager.registerOnPageChangeCallback(onPageChangeCallback)
    }
}
