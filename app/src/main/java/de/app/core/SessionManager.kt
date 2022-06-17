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
        dataSource.setCurrentUser(it.userId)
    }

    suspend fun addAccount(user: User, pin: String){
        dataSource.add(user, pin)
    }

    suspend fun removeAccount(userId: String): Result<Unit> {
        return dataSource.remove(userId)
    }

    suspend fun login(userId: String, pin: String): Result<User> {
        return dataSource.login(userId, pin).onSuccess { login(it) }
    }


    suspend fun getAccountById(userId: String): Result<UserHeader>{
        return dataSource.getUserById(userId)
    }

    suspend fun getAccounts(): List<UserHeader> {
        return dataSource.getAccounts()
    }

}