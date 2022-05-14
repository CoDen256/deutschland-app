package de.app.core

import de.app.data.Result
import de.app.data.model.Account

class SessionManager(val dataSource: LoginDataSource) {
    // in-memory cache of the loggedInUser object
    var currentAccount: Account? = null
        private set

    val isLoggedIn: Boolean
        get() = currentAccount != null

    init {
        currentAccount = null
    }

    fun logout() {
        currentAccount = null
        dataSource.logout()
    }

    fun login(username: String, password: String): Result<Account> {
        // handle login
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            currentAccount = result.data
        }

        return result
    }

}