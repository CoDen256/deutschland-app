package de.app.data.model.account

import java.io.Serializable

data class AccountHeader(
    val name: String,
    val surname: String,
    val accountId: String
): Serializable
