package de.app.api

import de.app.data.model.adminservice.AdministrativeService
import de.app.data.model.adminservice.AdministrativeServiceProvider
import de.app.data.model.adminservice.ApplicationForm
import de.app.data.model.adminservice.SubmittedForm

interface AdministrativeServiceRegistry {

    fun getAllProviders(): List<AdministrativeServiceProvider>
    fun getAllServices(): List<AdministrativeService>

    fun getApplicationForm(service: AdministrativeService): ApplicationForm
    fun sendApplicationForm(service: AdministrativeService, submittedForm: SubmittedForm)
}

