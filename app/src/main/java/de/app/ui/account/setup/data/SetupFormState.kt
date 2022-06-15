package de.app.ui.account.setup.data

/**
 * Data validation state of the login form.
 */
data class SetupFormState(
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)