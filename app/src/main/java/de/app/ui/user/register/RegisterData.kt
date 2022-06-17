package de.app.ui.user.register

import de.app.api.account.SecretToken
import de.app.data.model.UserType

data class RegisterFormState(
    val accountIdError: Int? = null,
    val enterIdHintText: Int? = null,
    val isDataValid: Boolean = false
)

data class RegisterUserView(
    val accountSecretToken: SecretToken,
    val type: UserType
)