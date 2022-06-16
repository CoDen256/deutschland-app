package de.app.ui.account.setup

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.app.R
import de.app.api.account.CitizenServiceAccountRepository
import de.app.core.SessionManager
import de.app.data.model.Account
import de.app.data.model.AccountHeader
import de.app.data.model.Address
import de.app.ui.account.setup.data.SetupFormState
import de.app.ui.account.setup.data.SetupResult
import de.app.ui.account.setup.data.SetupUserView
import javax.inject.Inject

class AccountSetPINViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val citizenRepo: CitizenServiceAccountRepository,
    ) : ViewModel() {

    val loginFormState = MutableLiveData<SetupFormState>()
    val loginResult = MutableLiveData<SetupResult>()

    fun login(accountId: String, pin: String) {
        val account = citizenRepo.getCitizenAccount(accountId).map {
            Account(
                it.accountId,
                it.displayName,
                Address(it.city, it.country, it.postalCode),
                Account.Type.CITIZEN
            )
        }.getOrThrow()
//        val result = sessionManager.addAccount(account, pin)
//        loginResult.value =
//            SetupResult(success = SetupUserView(account = account))
    }

    fun loginDataChanged(accountId: String, password: String) {
        if (!isPasswordValid(password)) {
            loginFormState.value = SetupFormState(passwordError = R.string.invalid_password)
        } else {
            loginFormState.value = SetupFormState(isDataValid = true)
        }
    }

    private fun isPasswordValid(pin: String): Boolean {
        return pin.length == 4 && pin.isDigitsOnly()
    }

    fun getAccountHeader(accountId: String): Result<AccountHeader> {
        return null!!//sessionManager.getAccountById(accountId)
    }
}