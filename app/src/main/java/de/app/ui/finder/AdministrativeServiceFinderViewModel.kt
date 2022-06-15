package de.app.ui.finder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.app.core.config.BaseAdministrativeServiceRegistry
import de.app.api.service.AdministrativeService

class AdministrativeServiceFinderViewModel :ViewModel(){
    val address: String = "Merseburg" // from service account
    private val registry = BaseAdministrativeServiceRegistry()

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