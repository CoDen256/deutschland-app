package de.app.ui.user.set.data

import de.app.data.model.User

/**
 * User details post authentication that is exposed to the UI
 */
data class SetupUserView(
    val account: User
)