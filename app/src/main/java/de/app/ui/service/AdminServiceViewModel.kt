package de.app.ui.service

import androidx.lifecycle.ViewModel
import de.app.api.AdministrativeServiceRegistry
import de.app.data.model.adminservice.AdministrativeService
import de.app.data.model.adminservice.ApplicationForm
import de.app.api.dummy.BaseAdministrativeServiceRegistry

class AdminServiceViewModel : ViewModel() {

    private val registry: AdministrativeServiceRegistry = BaseAdministrativeServiceRegistry()
    private val service: AdministrativeService = registry.getAllServices()[0]
    val applicationForm: ApplicationForm = registry.getApplicationForm(service)
}