package de.app.ui.finder

import android.content.Context
import androidx.lifecycle.*
import de.app.api.account.ServiceAccount
import de.app.api.account.CitizenServiceAccount
import de.app.api.account.CompanyServiceAccount
import de.app.api.service.AdministrativeService
import de.app.api.service.AdministrativeServiceRegistry
import de.app.data.model.Address
import de.app.geo.LocationRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AdministrativeServiceFinderViewModel @Inject constructor(
    private val registry: AdministrativeServiceRegistry,
    private val locationRepository: LocationRepository
) : ViewModel() {

    val services = MutableLiveData<List<AdministrativeService>>()
    val currentAddress = MutableLiveData<String>()
    val currentQuery = MutableLiveData<String>()

    fun search(account: ServiceAccount, searchQuery: String, address: String) {
        val result = getServicesForAccount(account)
        services.value = result.filter {
            it.name.containsIgnoreCase(searchQuery) || it.description.containsIgnoreCase(searchQuery)
        }.filter {
            it.address.city.containsIgnoreCase(address)
        }
    }
    private fun String.containsIgnoreCase(other: String): Boolean {
        return lowercase().contains(other.lowercase())
    }

    private fun getServicesForAccount(accountInfo: ServiceAccount): List<AdministrativeService> {
        return when(accountInfo) {
            is CitizenServiceAccount -> registry.getAllCitizenServices()
            is CompanyServiceAccount -> registry.getAllCompanyServices()
        }
    }

    fun init(context: Context, account: ServiceAccount) {
        viewModelScope.launch {
            locationRepository.requestSimplifiedAddress(context).addOnSuccessListener { result ->
                val address: Address = result.getOrDefault(account.address)
                currentAddress.value = address.city
                currentQuery.value = ""
            }
        }
    }

}