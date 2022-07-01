package de.app.data.model

open class UserHeader(
    val userId: String,
    val displayName: String,
)

class User(
    userId: String,
    displayName: String,
    val accountSecretToken: String,
    val address: Address,
    val type: UserType,
): UserHeader(userId, displayName)

enum class UserType { CITIZEN, COMPANY }

