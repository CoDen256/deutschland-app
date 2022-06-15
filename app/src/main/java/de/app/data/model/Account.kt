package de.app.data.model

open class AccountHeader(
    val id: String,
    val displayName: String
)

data class Account(
    val accountId: String,
    val accountDisplayName: String,
    val address: Address,
    val type: Type,
): AccountHeader(accountId, accountDisplayName) {
    enum class Type { CITIZEN, COMPANY }
}

