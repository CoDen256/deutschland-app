package de.app.api.applications

/**
 * Application service
 *
 * @constructor Create empty Application service
 */
interface ApplicationService {
    /**
     * Get all applications by account id
     *
     * @param accountId
     * @return
     */
    fun getAllApplicationsByAccountId(accountId: String): List<Application>

    /**
     * Add application for account id
     *
     * @param accountId
     * @param application
     * @return
     */// THIS IS USED BY Service Provider, not the application
    fun addApplicationForAccountId(accountId: String, application: Application): Result<Unit>
}