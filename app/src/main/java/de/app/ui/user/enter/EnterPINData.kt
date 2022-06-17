package de.app.ui.user.enter

data class EnterPINFormState(
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)

class EnterPINView