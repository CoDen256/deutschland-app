package de.app.ui.geo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class FragmentAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment =
        when (position) {
            1 -> {
                GeoDataSearchFragment()
            }
            else -> {
                GeoDataMapFragment()
            }
        }
}
