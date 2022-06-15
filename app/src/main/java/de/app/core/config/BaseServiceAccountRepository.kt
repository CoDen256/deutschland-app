package de.app.core.config

import de.app.api.account.CitizenAccountInfo
import de.app.api.account.CitizenServiceAccountRepository
import de.app.api.account.CompanyAccountInfo
import de.app.api.account.CompanyServiceAccountRepository
import de.app.data.Result
import de.app.data.Result.Companion.asResultOrThrow
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Singleton

val citizens = listOf(
    CitizenAccountInfo(
        "user-alpha", "Alpha Beta",
        "Merseburg", "06217", "Germany",
        "Alpha", "Beta", "Frau"
    ),
    CitizenAccountInfo(
        "user-bob", "Uncle Bob",
        "Halle", "06108", "Germany",
        "Uncle", "Bob", "Herr"
    ),
    CitizenAccountInfo(
        "user-delta", "Delta Zeta",
        "Leipzig", "04103", "Germany",
        "Delta", "Zeta", ""
    )
)

val companies = listOf(
    CompanyAccountInfo(
        "comp-yota", "Yota Gmbh",
        "Leipzig", "04103", "Germany",
        "Yota Gmbh Inc."
    ),
    CompanyAccountInfo(
        "comp-kappa", "Kappa Gmbh",
        "Bakhmut", "84500", "Ukraine",
        "Kappa Gmbh Inc."
    )
)

@Singleton
class BaseServiceAccountRepository @Inject constructor():
    CitizenServiceAccountRepository,
    CompanyServiceAccountRepository {
    override fun getCitizenAccount(accountId: String): Result<CitizenAccountInfo> {
        return citizens.find { it.accountId == accountId }.asResultOrThrow(createNotFoundException(accountId))
    }

    private fun createNotFoundException(accountId: String) =
        IllegalArgumentException("Account with id: $accountId was not found on the repository")

    override fun getCompanyAccount(accountId: String): Result<CompanyAccountInfo> {
        return companies.find { it.accountId == accountId }.asResultOrThrow(createNotFoundException(accountId))
    }
}