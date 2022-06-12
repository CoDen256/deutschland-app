package de.app.ui.finder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.app.api.dummy.BaseAdministrativeServiceRegistry
import de.app.data.model.service.AdministrativeService

class AdministrativeServiceFinderViewModel :ViewModel(){
    private val registry = BaseAdministrativeServiceRegistry()

    val readData: LiveData<List<AdministrativeService>> =
        MutableLiveData(registry.getAllServices())

    fun search(searchQuery: String, address: String): LiveData<List<AdministrativeService>> {
        return MutableLiveData(registry.getAllServices().filter {
            it.name.contains(searchQuery) || it.description.contains(searchQuery)
        }.filter {
            address.isEmpty() ||
                    (address.contains(it.address.city) || address.contains(it.address.postalCode))
        })
    }

}