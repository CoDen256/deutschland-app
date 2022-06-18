package de.app.ui.finder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.app.api.account.CitizenServiceAccountRepository
import de.app.api.account.SecretToken
import de.app.core.config.BaseAdministrativeServiceRegistry
import de.app.api.service.AdministrativeService
import de.app.api.service.AdministrativeServiceRegistry
import de.app.core.SessionManager
import de.app.data.model.Address
import javax.inject.Inject

class AdministrativeServiceFinderViewModel @Inject constructor(
    private val registry: AdministrativeServiceRegistry,
    private val citizenRepo: CitizenServiceAccountRepository,
    private val companyRepo: CitizenServiceAccountRepository,
    private val sessionManager: SessionManager
):ViewModel(){
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

    suspend fun getAddress(): Result<Address>{
        val currentUser = sessionManager.currentUser!!
        return citizenRepo.getCitizenAccount(SecretToken(currentUser.accountSecretToken))
            .map {
            it.address
        }
    }


}