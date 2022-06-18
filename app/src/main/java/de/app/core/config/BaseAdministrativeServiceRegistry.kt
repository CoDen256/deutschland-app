package de.app.core.config

import de.app.api.service.AdministrativeService
import de.app.api.service.AdministrativeServiceProvider
import de.app.api.service.AdministrativeServiceRegistry
import de.app.api.service.form.Form
import de.app.api.service.submit.SubmittedForm
import de.app.core.successOrElse
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextInt


class BaseAdministrativeServiceRegistry : AdministrativeServiceRegistry {

    private val services = generateServices(20)

    private val forms = services.map{it.id}.associateWith {
        Form(generateFields(nextInt(30)), nextBoolean())
    }

    override fun getAllProviders(): List<AdministrativeServiceProvider> {
        TODO("Not yet implemented")
    }

    override fun getProviderById(id: String): Result<AdministrativeServiceProvider> {
        TODO("Not yet implemented")
    }

    override fun getAllServices(): List<AdministrativeService> {
        return services
    }

    override fun getServiceById(id: String): Result<AdministrativeService> {
        return services.find { id == it.id }.successOrElse()
    }

    override fun getApplicationForm(service: AdministrativeService): Result<Form> {
        return forms[service.id].successOrElse()
    }

    override fun sendApplicationForm(
        service: AdministrativeService,
        submittedForm: SubmittedForm
    ): Result<Unit> {
        return Result.success(Unit)
    }
}

