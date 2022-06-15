package de.app.api.account

sealed interface AccountInfo{
    val accountId: String
    val displayName: String
    val city: String
    val postalCode: String
    val country: String
}

data class CitizenAccountInfo(
    override val accountId: String,
    override val displayName: String,
    override val city: String,
    override val postalCode: String,
    override val country: String,
    val firstName: String,
    val surname: String,
    val formOfAddress: String
): AccountInfo

data class CompanyAccountInfo(
    override val accountId: String,
    override val displayName: String,
    override val city: String,
    override val postalCode: String,
    override val country: String,
    val fullName: String,
): AccountInfo