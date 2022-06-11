package de.app.ui.geo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mapbox.mapboxsdk.Mapbox
import de.app.databinding.FragmentGeoDataBinding

class GeoDataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Mapbox.getInstance(requireActivity(), null)
        val binding = FragmentGeoDataBinding.inflate(inflater, container, false)

        val geoDataPager = binding.geoDataPager

        geoDataPager.isUserInputEnabled = false
        geoDataPager.adapter = FragmentAdapter(this)
        TabLayoutMediator(binding.tabLayout, geoDataPager) {
            tab, pos -> tab.text = when (pos) {
                1 -> "Search"
                else -> "Map"
            }
        }.attach()

        return binding.root
    }





}