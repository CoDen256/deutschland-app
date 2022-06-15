package de.app.api.account

import de.app.data.Result

interface CompanyServiceAccountRepository {
    fun getCompanyAccount(accountId: String): Result<CompanyAccountInfo>
}