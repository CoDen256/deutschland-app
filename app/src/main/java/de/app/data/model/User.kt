package de.app.data.model

open class UserHeader(
    val userId: String,
    val displayName: String,
    val icon: String
)

class User(
    userId: String,
    displayName: String,
    icon: String,
    val accountSecretToken: String,
    val address: Address,
    val type: UserType,
): UserHeader(userId, displayName, icon)

enum class UserType { CITIZEN, COMPANY }

