package de.app.api

import de.app.data.Result
import de.app.data.model.account.Account

interface CompanyServiceAccountRepository {
    fun getCompanyAccount(accountId: String): Result<Account>
}