package de.app.api.service

import de.app.api.service.form.Form
import de.app.api.service.submit.SubmittedForm

interface AdministrativeServiceRegistry {

    fun getAllProviders(): List<AdministrativeServiceProvider>
    fun getAllServices(): List<AdministrativeService>

    fun getProviderById(id: String): Result<AdministrativeServiceProvider>
    fun getServiceById(id: String): Result<AdministrativeService>

    fun getApplicationForm(service: AdministrativeService): Result<Form>
    fun sendApplicationForm(service: AdministrativeService, submittedForm: SubmittedForm): Result<Unit>
}

