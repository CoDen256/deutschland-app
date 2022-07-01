package de.app.core.config

import de.app.api.account.*
import de.app.core.config.DataGenerator.Companion.citizens
import de.app.core.config.DataGenerator.Companion.companies
import de.app.core.successOrElse
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BaseServiceAccountRepository @Inject constructor() :
    CitizenServiceAccountRepository,
    CompanyServiceAccountRepository {
    override fun getCitizenAccountSecretToken(accountId: String): Result<SecretToken> {
        return findAccountSecretToken(citizens, accountId)
    }

    override fun getCitizenAccount(secretToken: SecretToken): Result<CitizenServiceAccount> {
        return findAccountByToken(citizens, secretToken)
    }

    override fun getCompanyAccountSecretToken(accountId: String): Result<SecretToken> {
        return findAccountSecretToken(companies, accountId)
    }

    override fun getCompanyAccount(secretToken: SecretToken): Result<CompanyServiceAccount> {
        return findAccountByToken(companies, secretToken)
    }

    private fun <T: ServiceAccount> findAccountSecretToken(accounts: Map<SecretToken, T>, accountId: String): Result<SecretToken>{
        return accounts.entries.find { it.value.accountId == accountId }?.key
            .successOrElse(accountNotFound(accountId))
    }
    private fun <T: ServiceAccount> findAccountByToken(accounts: Map<SecretToken, T>, secretToken: SecretToken): Result<T>{
        return accounts[secretToken].successOrElse(secretTokenInvalid(secretToken))
    }

    private fun accountNotFound(accountId: String) =
        IllegalArgumentException("Account with id: $accountId was not found on the repository")

    private fun secretTokenInvalid(secretToken: SecretToken) =
        IllegalArgumentException("Secret token $secretToken is invalid")

}