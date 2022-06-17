package de.app.ui.user.set.data

/**
 * Authentication result : success (user details) or error message.
 */
data class SetupResult(
    val success: SetupUserView? = null,
    val error: String? = null
)