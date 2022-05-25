package de.app.ui.service.data

/**
 * Data validation state of the login form.
 */
data class FormState(
    val birthdayError: String? = null,
    val isDataValid: Boolean = false
)