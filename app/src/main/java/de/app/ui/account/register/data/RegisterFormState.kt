package de.app.ui.account.register.data

/**
 * Data validation state of the login form.
 */
data class RegisterFormState(
    val accountIdError: Int? = null,
    val enterIdText: Int? = null,
    val isDataValid: Boolean = false
)