package de.app.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class Account(
    val accountId: String,
    val name: String,
    val surname: String,
    val formOfAddress: String,
    val type: Type,
    // address?
) {
    enum class Type {
        CITIZEN, COMPANY
    }
}

