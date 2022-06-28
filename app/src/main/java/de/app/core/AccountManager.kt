package de.app.core

import android.app.Activity
import de.app.api.account.AccountInfo
import de.app.api.account.CitizenServiceAccountRepository
import de.app.api.account.CompanyServiceAccountRepository
import de.app.api.account.SecretToken
import de.app.data.model.User
import de.app.data.model.UserHeader
import de.app.data.model.UserType
import de.app.ui.user.LoginActivity
import de.app.ui.util.runActivity
import de.app.ui.util.toast
import java.lang.IllegalArgumentException

class AccountManager(
    private val manager: SessionManager,
    private val citizenRepo: CitizenServiceAccountRepository,
    private val companyRepo: CompanyServiceAccountRepository
) {

    suspend fun getAccountForUserHeader(userHeader: UserHeader): Result<AccountInfo> {
        return getAccountForUserId(userHeader.userId)
    }

    suspend fun getAccountForUserId(userId: String): Result<AccountInfo> {
        return manager.getUserById(userId).flatMap {
            getAccountForUser(it)
        }
    }

    fun getAccountForUser(user: User) = when (user.type) {
        UserType.CITIZEN -> citizenRepo.getCitizenAccount(getToken(user))
        UserType.COMPANY -> companyRepo.getCompanyAccount(getToken(user))
    }

    private fun getToken(user: User): SecretToken {
        return SecretToken(user.accountSecretToken)
    }

    fun getCurrentAccount(): Result<AccountInfo>{
        return manager.currentUser?.let {
            return getAccountForUser(it)
        } ?: Result.failure(IllegalArgumentException("No user logged in"))
    }

}


