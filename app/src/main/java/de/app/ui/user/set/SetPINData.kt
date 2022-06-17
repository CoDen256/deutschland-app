package de.app.ui.user.set

import de.app.data.model.User

data class SetPINFormState(
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)

data class SetPINUserView(
    val user: User
)