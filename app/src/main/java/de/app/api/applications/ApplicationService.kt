package de.app.api.applications

interface ApplicationService {
    fun getAllApplicationsByAccountId(accountId: String): List<Application>

    // THIS IS USED BY Service Provider, not the application
    fun addApplicationForAccountId(accountId: String, application: Application): Result<Unit>
}