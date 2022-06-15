package de.app.core

import de.app.data.model.Account
import de.app.data.model.AccountHeader

class SessionManager (private val dataSource: AccountDataSource) {
    var currentAccount: Account? = null
        private set

    init { updateCurrentAccount(); }
    fun updateCurrentAccount() {
        currentAccount = dataSource.getCurrent().getOrNull()
    }

    val isLoggedIn: Boolean
        get() = currentAccount != null

    fun logout() {
        currentAccount = null
        dataSource.removeCurrent()
    }

    private fun login(it: Account) {
        currentAccount = it
        dataSource.setCurrent(it.accountId)
    }

    fun addAccount(account: Account, pin: String){
        dataSource.add(account, pin)
    }

    fun removeAccount(accountId: String): Result<Unit> {
        return dataSource.remove(accountId)
    }

    fun login(accountId: String, pin: String): Result<Account> {
        return dataSource.login(accountId, pin).onSuccess { login(it) }
    }


    fun getAccountById(accountId: String): Result<AccountHeader>{
        return dataSource.getAccountById(accountId);
    }

    fun getAccounts(): List<AccountHeader> {
        return dataSource.getAccounts()
    }

}