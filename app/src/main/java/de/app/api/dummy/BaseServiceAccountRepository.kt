package de.app.api.dummy

import de.app.api.CitizenServiceAccountRepository
import de.app.api.CompanyServiceAccountRepository
import de.app.data.Result
import de.app.data.model.account.Account
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseServiceAccountRepository @Inject constructor(): CitizenServiceAccountRepository, CompanyServiceAccountRepository {
    override fun getCitizenAccount(accountId: String): Result<Account> {
        return Result.Success(
            Account(
                "de-app-user",
                "Alpha",
                "DeAppMann",
                Account.Type.CITIZEN
            )
        )
    }

    override fun getCompanyAccount(accountId: String): Result<Account> {
        return Result.Success(
            Account(
                "de-app-company",
                "Delta",
                "DeAppGmbh",
                Account.Type.COMPANY
            )
        )
    }
}