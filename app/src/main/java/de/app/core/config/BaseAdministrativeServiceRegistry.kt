package de.app.core.config

import de.app.api.service.AdministrativeService
import de.app.api.service.AdministrativeServiceProvider
import de.app.api.service.AdministrativeServiceRegistry
import de.app.api.service.form.Form
import de.app.api.service.submit.SubmittedForm
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextInt


class BaseAdministrativeServiceRegistry : AdministrativeServiceRegistry {

    private val services = generateServices(20)

    override fun getAllProviders(): List<AdministrativeServiceProvider> {
        TODO("Not yet implemented")
    }

    override fun getAllServices(): List<AdministrativeService> {
        return services
    }

    override fun getApplicationForm(service: AdministrativeService): Form {
        return Form(generateFields(nextInt(30)), nextBoolean())
    }

    override fun sendApplicationForm(
        service: AdministrativeService,
        submittedForm: SubmittedForm
    ): Result<Unit> {
        return Result.success(Unit)
    }
}

