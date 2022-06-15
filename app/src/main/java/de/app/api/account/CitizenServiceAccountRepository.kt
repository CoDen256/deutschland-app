package de.app.api.account

import de.app.data.Result

interface CitizenServiceAccountRepository {
    fun getCitizenAccount(accountId: String): Result<CitizenAccountInfo>
}