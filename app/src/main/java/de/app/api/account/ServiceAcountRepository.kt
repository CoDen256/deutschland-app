package de.app.api.account

interface CitizenServiceAccountRepository {
    fun getCitizenAccountSecretToken(accountId: String): Result<SecretToken>
    fun getCitizenAccount(secretToken: SecretToken): Result<CitizenServiceAccount>
}

interface CompanyServiceAccountRepository {
    fun getCompanyAccountSecretToken(accountId: String): Result<SecretToken>
    fun getCompanyAccount(secretToken: SecretToken): Result<CompanyServiceAccount>
}