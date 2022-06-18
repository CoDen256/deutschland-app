package de.app.api.account

import de.app.data.model.Address


data class SecretToken(
    val token: String
)

sealed interface AccountInfo{
    val accountId: String
    val displayName: String
    val address: Address
}

data class CitizenAccountInfo(
    override val accountId: String,
    override val displayName: String,
    override val address: Address,
    val firstName: String,
    val surname: String,
    val formOfAddress: String
): AccountInfo

data class CompanyAccountInfo(
    override val accountId: String,
    override val displayName: String,
    override val address: Address,
    val fullName: String,
): AccountInfo