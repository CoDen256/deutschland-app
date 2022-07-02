package de.app.config

import de.app.api.applications.Application
import de.app.api.applications.ApplicationService
import de.app.config.DataGenerator.Companion.generateApplications
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseApplicationService @Inject constructor(): ApplicationService {
    private val applications = ArrayList(generateApplications(40, listOf()))
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