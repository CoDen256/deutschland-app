package de.app.core.config

import de.app.api.account.*
import de.app.core.successOrElse
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Singleton

val citizens = mapOf(
    SecretToken("ua") to CitizenAccountInfo(
        "user-alpha", "Alpha Beta",
        "Merseburg", "06217", "Germany",
        "Alpha", "Beta", "Frau"
    ),
    SecretToken("ub") to CitizenAccountInfo(
        "user-bob", "Uncle Bob",
        "Halle", "06108", "Germany",
        "Uncle", "Bob", "Herr"
    ),
    SecretToken("ud") to CitizenAccountInfo(
        "user-delta", "Delta Zeta",
        "Leipzig", "04103", "Germany",
        "Delta", "Zeta", ""
    )
)

val companies = mapOf(
    SecretToken("cy") to CompanyAccountInfo(
        "comp-yota", "Yota Gmbh",
        "Leipzig", "04103", "Germany",
        "Yota Gmbh Inc."
    ),
    SecretToken("ck") to CompanyAccountInfo(
        "comp-kappa", "Kappa Gmbh",
        "Bakhmut", "84500", "Ukraine",
        "Kappa Gmbh Inc."
    )
)

@Singleton
class BaseServiceAccountRepository @Inject constructor() :
    CitizenServiceAccountRepository,
    CompanyServiceAccountRepository {
    override fun getCitizenAccountSecretToken(accountId: String): Result<SecretToken> {
        return findAccountSecretToken(citizens, accountId)
    }

    override fun getCitizenAccount(secretToken: SecretToken): Result<CitizenAccountInfo> {
        return findAccountByToken(citizens, secretToken)
    }

    override fun getCompanyAccountSecretToken(accountId: String): Result<SecretToken> {
        return findAccountSecretToken(companies, accountId)
    }

    override fun getCompanyAccount(secretToken: SecretToken): Result<CompanyAccountInfo> {
        return findAccountByToken(companies, secretToken)
    }

    private fun <T: AccountInfo> findAccountSecretToken(accounts: Map<SecretToken, T>, accountId: String): Result<SecretToken>{
        return accounts.entries.find { it.value.accountId == accountId }?.key
            .successOrElse(accountNotFound(accountId))
    }
    private fun <T: AccountInfo> findAccountByToken(accounts: Map<SecretToken, T>, secretToken: SecretToken): Result<T>{
        return accounts[secretToken].successOrElse(secretTokenInvalid(secretToken))
    }

    private fun accountNotFound(accountId: String) =
        IllegalArgumentException("Account with id: $accountId was not found on the repository")

    private fun secretTokenInvalid(secretToken: SecretToken) =
        IllegalArgumentException("Secret token $secretToken is invalid")

}