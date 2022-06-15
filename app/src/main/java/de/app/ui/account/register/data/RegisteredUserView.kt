package de.app.ui.account.register.data

import de.app.api.account.AccountInfo

/**
 * User details post authentication that is exposed to the UI
 */
data class RegisteredUserView(
    val account: AccountInfo
)