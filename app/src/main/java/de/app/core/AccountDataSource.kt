package de.app.core

import de.app.data.Result
import de.app.data.model.account.Account
import de.app.data.model.account.AccountHeader
import java.io.IOException
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.HashMap

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class AccountDataSource {

    companion object {
        private val accounts: Map<Account, String> = mapOf(
            Account(UUID.randomUUID().toString(),"Jane", "Doe",
                "Herr", Account.Type.CITIZEN) to "0000",
            Account(UUID.randomUUID().toString(),"NoJane", "NoDoe",
                "Herr", Account.Type.CITIZEN) to "0000"
        )
        private val infoToAccount: Map<String, Pair<Account, String>> = run {
            val map = HashMap<String, Pair<Account, String>>()
            for ((account, pin) in accounts) {
                    map[account.accountId] = account to pin
                }
            map
        }

        private val ACCOUNT_INFO: List<AccountHeader> = accounts.keys.map { AccountHeader(it.name, it.surname, it.accountId) }

    }

    fun add(account: Account, pin: String){

    }

    fun login(accountId: String, pin: String): Result<Account> {
        try {
            val pair = infoToAccount[accountId]
                ?: return Result.Error(IllegalArgumentException("No account with $accountId exists"))
            val (account, expectedPin) = pair
            if (expectedPin != pin) return Result.Error(IllegalArgumentException("Wrong PIN"))
            return Result.Success(account)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun getAccounts(): List<AccountHeader> {
        return ACCOUNT_INFO
    }

    fun remove(account: Account){

    }
}