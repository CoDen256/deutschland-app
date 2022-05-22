package de.app.ui.account.login.data

import de.app.data.model.account.Account

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val account: Account
)