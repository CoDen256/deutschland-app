package de.app.ui.account.setup.data

import de.app.data.model.Account

/**
 * User details post authentication that is exposed to the UI
 */
data class SetupUserView(
    val account: Account
)