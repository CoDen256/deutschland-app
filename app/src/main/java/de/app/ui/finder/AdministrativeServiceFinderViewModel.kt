package de.app.ui.finder

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import de.app.api.service.AdministrativeService
import de.app.api.service.AdministrativeServiceRegistry
import de.app.core.onSuccess
import de.app.data.model.Address
import de.app.geo.LocationRepository
import de.app.ui.util.geoDecode
import de.app.ui.util.simplify
import javax.inject.Inject

class AdministrativeServiceFinderViewModel @Inject constructor(
    private val registry: AdministrativeServiceRegistry,
    private val locationRepository: LocationRepository
) : ViewModel() {

    fun search(searchQuery: String, address: String): LiveData<List<AdministrativeService>> {
        return MutableLiveData(registry.getAllServices().filter {
            it.name.lowercase().contains(searchQuery.lowercase()) ||
                    it.description.lowercase().contains(searchQuery.lowercase())
        }.filter {
            (it.address.city.lowercase().contains(address.lowercase()))
        })
    }

    fun requestAddress(context: Context): Task<Result<Address>> {
        return locationRepository.requestSimplifiedAddress(context)
    }

}