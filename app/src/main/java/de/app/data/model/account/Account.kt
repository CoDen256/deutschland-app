package de.app.data.model.account

import java.io.Serializable

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class Account(
    val accountId: String,
    val name: String,
    val surname: String,
    val type: Type,
    // address?
): Serializable {
    enum class Type {
        CITIZEN, COMPANY
    }
}

