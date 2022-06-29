package de.app.api.account

import de.app.data.model.Address
import java.time.LocalDate


data class SecretToken(
    val token: String
)

sealed interface AccountInfo{
    val accountId: String
    val displayName: String
    val address: Address
}

data class CitizenAccountInfo(
    override val accountId: String,
    override val displayName: String,
    override val address: Address,
    val firstName: String,
    val surname: String,
    val salutation: String,
    val dateOfBirth: LocalDate = LocalDate.now(),
    val nationality: String = "Ukraine",
    val placeOfBirth: Address = address,
    ): AccountInfo

data class CompanyAccountInfo(
    override val accountId: String,
    override val displayName: String,
    override val address: Address,
    val fullName: String,
    val foundedDate: LocalDate = LocalDate.now()
): AccountInfo