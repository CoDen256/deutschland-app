package de.app.api

import de.app.data.Result
import de.app.data.model.account.Account

interface CitizenServiceAccountRepository {
    fun getCitizenAccount(accountId: String): Result<Account>
}