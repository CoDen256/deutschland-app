package de.app.ui.account.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)