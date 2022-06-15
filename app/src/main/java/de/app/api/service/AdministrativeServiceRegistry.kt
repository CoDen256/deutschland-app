package de.app.api.service

import de.app.data.Result
import de.app.api.service.AdministrativeService
import de.app.api.service.AdministrativeServiceProvider
import de.app.api.service.form.Form
import de.app.api.service.submit.SubmittedForm

interface AdministrativeServiceRegistry {

    fun getAllProviders(): List<AdministrativeServiceProvider>
    fun getAllServices(): List<AdministrativeService>

    fun getApplicationForm(service: AdministrativeService): Form
    fun sendApplicationForm(service: AdministrativeService, submittedForm: SubmittedForm): Result<Any>
}

