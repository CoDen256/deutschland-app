package de.app.core

import de.app.core.db.AccountDao
import de.app.data.model.Account
import de.app.data.model.AccountHeader
import de.app.data.model.Address
import de.app.data.model.entities.AccountEntity
import de.app.data.model.entities.CredentialsEntity
import de.app.data.model.entities.CurrentLogin

class AccountDataSource(
    private val accountDao: AccountDao,
) {

    suspend fun getAccounts(): List<AccountHeader> {
        return accountDao.getAll().map {deserializeAccount(it)}
    }

    fun getCurrent(): Result<Account>{
        val currentLogin = accountDao.getCurrentLogin()
        if (currentLogin.isEmpty()) return Result.failure(IllegalStateException("No logged in account"))
        val accountId = currentLogin.first().accountId

        return getAccountById(accountId)
    }

    fun setCurrent(accountId: String) {
        val currentLogin = accountDao.getCurrentLogin()
        val newLogin = CurrentLogin(accountId)
        if (currentLogin.isNotEmpty()){
            accountDao.setCurrentLogin(currentLogin.first(), newLogin)
        }else {
            accountDao.insert(newLogin)
        }
    }

    fun removeCurrent(){
        val currentLogin = accountDao.getCurrentLogin()
        if (currentLogin.isNotEmpty()){
            accountDao.delete(currentLogin.first())
        }
    }

    fun remove(accountId: String): Result<Unit> {
        return getAccountEntityById(accountId).map {
            val credentials = accountDao.getCredentialsById(accountId) !!
            val currentLogin = accountDao.getCurrentLogin()
            if (currentLogin.isNotEmpty() && currentLogin.first().accountId == accountId) {
                accountDao.delete(it, credentials, currentLogin.first())
            }else{
                accountDao.delete(it, credentials)
            }
        }
    }

    fun login(accountId: String, pin: String): Result<Account> {
        val credentials =
            accountDao.getCredentialsById(accountId) ?: return accountNotFoundError(accountId)

        if (credentials.pin == pin){
            return getAccountById(accountId)
        }
        return Result.failure(IllegalStateException("Invalid pin"))
    }

    fun add(account: Account, pin: String) {
        accountDao.insert(
            serializeAccount(account),
            CredentialsEntity(account.accountId, pin)
        )
    }

    private fun getAccountEntityById(accountId: String): Result<AccountEntity> {
        return accountDao.getAccountById(accountId)?.success() ?: accountNotFoundError(accountId)
    }

    fun getAccountById(accountId: String): Result<Account> {
        return getAccountEntityById(accountId).map { deserializeAccount(it) }
    }

    private fun <T> accountNotFoundError(accountId: String): Result<T> =
        Result.failure(IllegalArgumentException("Account with id $accountId not found"))

    private fun serializeAccount(account: Account): AccountEntity {
        return AccountEntity(
            account.accountId,
            account.displayName,
            account.address.city,
            account.address.country,
            account.address.postalCode,
            account.type.name
        )
    }

    private fun deserializeAccount(account: AccountEntity): Account {
        return Account(
            account.id,
            account.displayName,
            Address(account.city, account.country, account.postalCode),
            Account.Type.valueOf(account.type)
        )
    }
}