package de.app.api

import de.app.data.Result
import de.app.data.model.service.AdministrativeService
import de.app.data.model.service.AdministrativeServiceProvider
import de.app.data.model.service.form.Form
import de.app.data.model.service.submit.SubmittedForm

interface AdministrativeServiceRegistry {

    fun getAllProviders(): List<AdministrativeServiceProvider>
    fun getAllServices(): List<AdministrativeService>

    fun getApplicationForm(service: AdministrativeService): Form
    fun sendApplicationForm(service: AdministrativeService, submittedForm: SubmittedForm): Result<Any>
}

