package de.app.ui.service.data.result

/**
 * Authentication result : success (user details) or error message.
 */
data class FormResult(
    val success: FormView? = null,
    val error: String? = null
)