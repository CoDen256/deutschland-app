package de.app.data.model

open class UserHeader(
    val userId: String,
    val displayName: String
)

class User(
    _userId: String,
    _displayName: String,
    val accountSecretToken: String,
    val address: Address,
    val type: Type,
): UserHeader(_userId, _displayName) {
    enum class Type { CITIZEN, COMPANY }
}

