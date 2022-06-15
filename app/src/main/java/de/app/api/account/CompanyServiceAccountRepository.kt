package de.app.api.account

interface CompanyServiceAccountRepository {
    fun getCompanyAccount(accountId: String): Result<CompanyAccountInfo>
}