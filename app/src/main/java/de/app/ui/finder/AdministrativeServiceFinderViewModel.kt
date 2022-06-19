package de.app.ui.finder

import android.content.ServiceConnection
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.app.api.account.CitizenServiceAccountRepository
import de.app.api.account.SecretToken
import de.app.core.config.BaseAdministrativeServiceRegistry
import de.app.api.service.AdministrativeService
import de.app.api.service.AdministrativeServiceRegistry
import de.app.core.SessionManager
import de.app.data.model.Address
import de.app.geo.ForegroundLocationServiceConnection
import de.app.geo.LocationRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AdministrativeServiceFinderViewModel @Inject constructor(
    private val registry: AdministrativeServiceRegistry,
    val locationRepository: LocationRepository
):ViewModel(){

    val isReceivingLocationUpdates = locationRepository.isReceivingLocationUpdates
    val lastLocation: StateFlow<Location?> = locationRepository.lastLocation



    val readData: LiveData<List<AdministrativeService>> =
        MutableLiveData(registry.getAllServices())

    fun search(searchQuery: String, address: String): LiveData<List<AdministrativeService>> {
        return MutableLiveData(registry.getAllServices().filter {
            it.name.lowercase().contains(searchQuery.lowercase()) ||
                    it.description.lowercase().contains(searchQuery.lowercase())
        }.filter {
                    (it.address.city.lowercase().contains(address.lowercase()))
        })
    }

}