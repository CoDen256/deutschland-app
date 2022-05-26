package de.app.ui.service.data.result

/**
 * Submitted form result : success (user details) or error message.
 */
data class FormResult(
    val success: FormView? = null,
    val error: String? = null
)