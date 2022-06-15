package de.app.api.account

interface CitizenServiceAccountRepository {
    fun getCitizenAccount(accountId: String): Result<CitizenAccountInfo>
}