package de.app.ui.user.enter.data

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)