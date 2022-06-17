package de.app.core

import de.app.core.db.UserDataSource
import de.app.data.model.User
import de.app.data.model.UserHeader

class SessionManager (private val dataSource: UserDataSource) {
    var currentUser: User? = null
        private set

    suspend fun init() { updateCurrentUser(); }
    suspend fun updateCurrentUser() {
        currentUser = dataSource.getCurrentUser().getOrNull()
    }

    val isLoggedIn: Boolean
        get() = currentUser != null

    suspend fun logout() {
        currentUser = null
        dataSource.removeCurrent()
    }

    private suspend fun login(it: User) {
        currentUser = it
        dataSource.setCurrentUser(it.accountId)
    }

    suspend fun addAccount(account: User, pin: String){
        dataSource.add(account, pin)
    }

    suspend fun removeAccount(accountId: String): Result<Unit> {
        return dataSource.remove(accountId)
    }

    suspend fun login(accountId: String, pin: String): Result<User> {
        return dataSource.login(accountId, pin).onSuccess { login(it) }
    }


    suspend fun getAccountById(accountId: String): Result<UserHeader>{
        return dataSource.getUserById(accountId)
    }

    suspend fun getAccounts(): List<UserHeader> {
        return dataSource.getAccounts()
    }

}