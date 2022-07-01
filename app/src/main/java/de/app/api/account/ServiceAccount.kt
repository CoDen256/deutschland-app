package de.app.api.account

import de.app.data.model.Address
import java.time.LocalDate

/**
 * Secret Token wird ver
 *
 * @property token
 * @constructor Create empty Secret token
 */
data class SecretToken(
    val token: String
)

sealed interface ServiceAccount{
    val accountId: String
    val displayName: String
    val address: Address
}

data class CitizenServiceAccount(
    override val accountId: String,
    override val displayName: String,
    override val address: Address,
    val firstName: String,
    val surname: String,
    val salutation: String,
    val dateOfBirth: LocalDate = LocalDate.now(),
    val nationality: String = "Ukraine",
    val placeOfBirth: Address = address,
    ): ServiceAccount

data class CompanyServiceAccount(
    override val accountId: String,
    override val displayName: String,
    override val address: Address,
    val fullName: String,
    val foundedDate: LocalDate = LocalDate.now()
): ServiceAccount