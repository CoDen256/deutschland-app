package de.app.ui.service.data.state

/**
 * Data validation state of the login form.
 */
data class FormState(
    val fieldStates: Map<String, FieldState>,
    val isDataValid: Boolean = false
)