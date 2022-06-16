package de.app.core

import de.app.core.db.AccountDataSource
import de.app.data.model.Account
import de.app.data.model.AccountHeader

class SessionManager (private val dataSource: AccountDataSource) {
    var currentAccount: Account? = null
        private set

    suspend fun init() { updateCurrentAccount(); }
    suspend fun updateCurrentAccount() {
        currentAccount = dataSource.getCurrent().getOrNull()
    }

    val isLoggedIn: Boolean
        get() = currentAccount != null

    suspend fun logout() {
        currentAccount = null
        dataSource.removeCurrent()
    }

    private suspend fun login(it: Account) {
        currentAccount = it
        dataSource.setCurrent(it.accountId)
    }

    suspend fun addAccount(account: Account, pin: String){
        dataSource.add(account, pin)
    }

    suspend fun removeAccount(accountId: String): Result<Unit> {
        return dataSource.remove(accountId)
    }

    suspend fun login(accountId: String, pin: String): Result<Account> {
        return dataSource.login(accountId, pin).onSuccess { login(it) }
    }


    suspend fun getAccountById(accountId: String): Result<AccountHeader>{
        return dataSource.getAccountById(accountId);
    }

    suspend fun getAccounts(): List<AccountHeader> {
        return dataSource.getAccounts()
    }

}