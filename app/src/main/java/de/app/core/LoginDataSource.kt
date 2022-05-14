package de.app.core

import de.app.data.Result
import de.app.data.model.Account
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(id: String, password: String): Result<Account> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = Account(java.util.UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}