package de.app.core

import de.app.data.Result
import de.app.data.model.account.Account
import de.app.data.model.account.AccountHeader

class SessionManager(val dataSource: AccountDataSource) {
    // in-memory cache of the loggedInUser object
    var currentAccount: Account? = null
        private set

    val isLoggedIn: Boolean
        get() = currentAccount != null


    fun logout() {
        currentAccount = null
    }

    fun login(accountId: String, pin: String): Result<Account> {
        // handle login
        val result = dataSource.login(accountId, pin)

        if (result is Result.Success) {
            currentAccount = result.data
        }

        return result
    }

}