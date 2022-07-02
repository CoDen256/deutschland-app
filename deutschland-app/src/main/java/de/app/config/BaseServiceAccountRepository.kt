package de.app.config

import de.app.api.account.*
import de.app.config.common.CitizenAccountDataSource
import de.app.config.common.CompanyAccountDataSource
import de.app.core.successOrElse
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BaseServiceAccountRepository @Inject constructor(
    source: AccountDataSource
) :
    CitizenServiceAccountRepository,
    CompanyServiceAccountRepository {

    private val citizens by lazy {
        source.citizens.associateBy { SecretToken(UUID.randomUUID().toString()) }
    }

    private val companies by lazy {
        source.companies.associateBy { SecretToken(UUID.randomUUID().toString()) }
    }
    override fun authenticateCitizen(accountId: String): Result<SecretToken> {
        return findAccountSecretToken(citizens, accountId)
    }

    override fun getCitizenAccount(secretToken: SecretToken): Result<CitizenServiceAccount> {
        return findAccountByToken(citizens, secretToken)
    }

    override fun authenticateCompany(accountId: String): Result<SecretToken> {
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


@Singleton
class AccountDataSource @Inject constructor(
    citizensDataSource: CitizenAccountDataSource,
    companiesDataSource: CompanyAccountDataSource
){
    val citizens by lazy {
        citizensDataSource.data
    }
    val companies by lazy {
        companiesDataSource.data
    }
    val all by lazy {
        companies + citizens
    }
}

