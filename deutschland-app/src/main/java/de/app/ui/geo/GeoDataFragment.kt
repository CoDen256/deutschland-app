package de.app.ui.geo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.databinding.FragmentGeoDataBinding
import de.app.ui.components.SimpleFragment
import de.app.ui.geo.filter.GeoDataFilterFragment
import de.app.ui.geo.map.GeoDataMapFragment
import javax.inject.Inject

@AndroidEntryPoint
class GeoDataFragment : SimpleFragment<FragmentGeoDataBinding>() {

    private val tabToName by lazy {
        listOf(
            getString(R.string.geo_search_tab),
            getString(R.string.get_map_tab)
        )
    }

    @Inject
    lateinit var viewModel: GeoDataViewModel

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentGeoDataBinding.inflate(inflater, container, false)

    override fun setup() {
        viewModel.init(requireContext())

        val geoDataPager = binding.geoDataPager

        geoDataPager.isUserInputEnabled = false
        geoDataPager.adapter = GeoDataFragmentCollection(this)

        TabLayoutMediator(binding.tabLayout, geoDataPager) { tab, pos -> tab.text = tabToName[pos] }.attach()

        geoDataPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                viewModel.tabState.value = state
            }
        })
        viewModel.tabRequested.observe(viewLifecycleOwner) {
            it ?: return@observe
            geoDataPager.setCurrentItem(it, true)
            viewModel.tabRequested.value = null
        }
    }
}

class GeoDataFragmentCollection(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val filter = GeoDataFilterFragment()
    private val map = GeoDataMapFragment()
    private val tabToFragment = listOf(filter, map)
    override fun getItemCount() =tabToFragment.size
    override fun createFragment(position: Int): Fragment = tabToFragment[position]
}
