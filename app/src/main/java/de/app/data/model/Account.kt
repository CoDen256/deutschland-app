package de.app.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class Account(
    val userId: String,
    val displayName: String
)