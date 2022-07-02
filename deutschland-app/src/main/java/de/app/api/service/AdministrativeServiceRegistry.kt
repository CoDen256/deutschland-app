package de.app.api.service

import de.app.api.account.ServiceAccount
import de.app.api.service.form.Form
import de.app.api.service.submit.SubmittedForm

interface AdministrativeServiceRegistry {

    fun getAllCitizenServices(): List<AdministrativeService>
    fun getAllCompanyServices(): List<AdministrativeService>

    fun getServiceById(id: String): Result<AdministrativeService>

    fun getApplicationForm(service: AdministrativeService): Result<Form>
    fun sendApplicationForm(account: ServiceAccount, service: AdministrativeService, submittedForm: SubmittedForm): Result<Unit>
}

