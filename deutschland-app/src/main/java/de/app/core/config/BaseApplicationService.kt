package de.app.core.config

import de.app.api.applications.Application
import de.app.api.applications.ApplicationService
import de.app.core.config.DataGenerator.Companion.generateApplications
import de.app.core.successOrElse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseApplicationService @Inject constructor(): ApplicationService {
    private val applications = ArrayList(generateApplications(40, BaseAdministrativeServiceRegistry.services))
    override fun getAllApplicationsByAccountId(accountId: String): List<Application> {
        return applications.filter { it.accountId == accountId }
    }

    override fun addApplicationForAccountId(
        accountId: String,
        application: Application
    ): Result<Unit> {
        applications.add(application)
        return Result.success(Unit)
    }
}