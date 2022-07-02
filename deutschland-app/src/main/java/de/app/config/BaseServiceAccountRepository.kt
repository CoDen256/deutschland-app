package de.app.config

import android.content.Context
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import de.app.api.account.*
import de.app.config.common.AssetDataSource
import de.app.config.common.CitizenAccountDataSource
import de.app.config.common.CompanyAccountDataSource
import de.app.core.successOrElse
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BaseServiceAccountRepository @Inject constructor(
    citizenRepo: CitizenRepoDataSource,
    companyRepo: CompanyRepoDataSource
) :
    CitizenServiceAccountRepository,
    CompanyServiceAccountRepository {

    private val citizens by lazy {
        mapOf(*citizenRepo.data.toTypedArray())
    }

    private val companies by lazy {
        mapOf(*companyRepo.data.toTypedArray())
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
        IllegalArgumentException("Secret token ${secretToken.token} is invalid")

}


@Singleton
class CitizenRepoDataSource @Inject constructor(
    @ApplicationContext context: Context,
    source: CitizenAccountDataSource
) :
    AssetDataSource<Pair<SecretToken, CitizenServiceAccount>, TokenBinding>(context, "binding/citizen-repo.json") {

    private val citizenById = source.data.associateBy { it.accountId }

    override fun map(origin: TokenBinding): Pair<SecretToken, CitizenServiceAccount> {
        return SecretToken(origin.token) to citizenById[origin.accountId]!!
    }

    override fun getJsonType(): Type = object : TypeToken<List<TokenBinding>>() {}.type
}
@Singleton
class CompanyRepoDataSource @Inject constructor(
    @ApplicationContext context: Context,
    source: CompanyAccountDataSource
) :
    AssetDataSource<Pair<SecretToken, CompanyServiceAccount>, TokenBinding>(context, "binding/company-repo.json") {

    private val companyById = source.data.associateBy { it.accountId }

    override fun map(origin: TokenBinding): Pair<SecretToken, CompanyServiceAccount> {
        return SecretToken(origin.token) to companyById[origin.accountId]!!
    }

    override fun getJsonType(): Type = object : TypeToken<List<TokenBinding>>() {}.type
}

data class TokenBinding(
    val accountId: String,
    val token: String
)
