package de.app.core

import de.app.data.Result
import de.app.data.model.Account
import de.app.core.db.AccountDao
import de.app.data.Result.Companion.asResult
import de.app.data.model.AccountHeader
import de.app.data.model.Address
import de.app.data.model.entities.AccountEntity
import de.app.data.model.entities.CredentialsEntity
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class AccountDataSource(
    private val accountDao: AccountDao,
) {

    fun add(account: Account, pin: String) {
        accountDao.insert(
            serializeAccount(account),
            CredentialsEntity(account.accountId, pin)
        )
    }

    fun login(accountId: String, pin: String): Result<Account> {
        val credentials =
            accountDao.getCredentialsById(accountId) ?: return accountNotFoundError(accountId)

        if (credentials.pin == pin){
            val acc = accountDao.getAccountById(accountId) ?: return accountNotFoundError(accountId)
            return deserializeAccount(acc).asResult()
        }
        return Result.Error(IllegalStateException("Invalid pin"))
    }

    fun remove(accountId: String): Result<Nothing> {
        val acc = accountDao.getAccountById(accountId) ?: return accountNotFoundError(accountId)
        val credentials = accountDao.getCredentialsById(accountId) !!
        accountDao.delete(acc, credentials)
        return Result.Success(null);
    }

    private fun accountNotFoundError(accountId: String) =
        Result.Error(IllegalArgumentException("Account with id $accountId not found"))

    fun getAccounts(): List<AccountHeader> {
        return accountDao.getAll().map {deserializeAccount(it)}
    }

    private fun serializeCredentials(info: Credentials): CredentialsEntity {
        return CredentialsEntity(info.account.accountId, info.pin)
    }

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

    private fun deserializeCredentials(info: CredentialsEntity, account: Account): Credentials {
        return Credentials(account, info.pin)
    }

    private fun deserializeAccount(account: AccountEntity): Account {
        return Account(
            account.id,
            account.displayName,
            Address(account.city, account.country, account.postalCode),
            Account.Type.valueOf(account.type)
        )
    }

    private inner class Credentials(
        val account: Account,
        val pin: String,
    )
}