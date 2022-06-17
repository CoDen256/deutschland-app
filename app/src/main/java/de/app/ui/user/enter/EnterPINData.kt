package de.app.ui.user.enter

import de.app.data.model.User

data class EnterPINFormState(
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)

data class EnterPINView(
    val account: User
)