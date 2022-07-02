package de.app.config

import android.content.Context
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import de.app.api.applications.Application
import de.app.api.applications.ApplicationService
import de.app.api.applications.ApplicationStatus
import de.app.config.DataGenerator.Companion.generateApplications
import de.app.config.common.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseApplicationService @Inject constructor(
    dataSource: ApplicationByAccountDataSource

): ApplicationService {
    private val applications by lazy {
        ArrayList(dataSource.data.flatten())
    }
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

@Singleton
class ApplicationByAccountDataSource @Inject constructor(
    @ApplicationContext context: Context,
    applications: ApplicationDataSource
) : AssetDataSource<List<Application>, ApplicationByAccount>(context, "origin/services.json") {

    private val applicationById: Map<Int, ApplicationAsset> = applications.data.associateBy { it.applicationId }

    override fun map(origin: ApplicationByAccount): List<Application> {
        return origin.applications.map {
            val app = applicationById[it.applicationId]!!
            Application(
                app.serviceId, origin.accountId,
                app.name, app.description,
                ApplicationStatus.valueOf(it.status),
                it.applicationDateTime
            )
        }
    }

    override fun getJsonType(): Type = object : TypeToken<List<ApplicationByAccount>>() {}.type
}

data class ApplicationByAccount(
    val accountId: String,
    val applications: List<ApplicationForAccount>
)

data class ApplicationForAccount(
    val applicationId: Int,
    val applicationDateTime: LocalDateTime,
    val status: String
)