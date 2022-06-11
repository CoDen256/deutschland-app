package de.app.ui.geo

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class GeoDataFragmentCollection(val fragment: Fragment, val geoDataPager: ViewPager2) : FragmentStateAdapter(fragment){

    val data = MutableLiveData<MapObjectInfo>()

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> {
                GeoDataSearchFragment(this)
            }
            else -> {
                GeoDataMapFragment(data)
            }
        }

    fun moveToMap(data: MapObjectInfo){
        geoDataPager.setCurrentItem(1, true)
        this.data.value = data
    }

    fun registerOnPageChangeCallback(onPageChangeCallback: ViewPager2.OnPageChangeCallback) {
        geoDataPager.registerOnPageChangeCallback(onPageChangeCallback)
    }
}
