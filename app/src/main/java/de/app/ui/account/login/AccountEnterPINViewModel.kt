package de.app.ui.account.login

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.app.R
import de.app.core.SessionManager
import de.app.data.model.AccountHeader
import de.app.ui.account.login.data.LoggedInUserView
import de.app.ui.account.login.data.LoginFormState
import de.app.ui.account.login.data.LoginResult
import javax.inject.Inject

class AccountEnterPINViewModel @Inject constructor(private val sessionManager: SessionManager) : ViewModel() {

    val loginFormState = MutableLiveData<LoginFormState>()
    val loginResult = MutableLiveData<LoginResult>()

    fun getAccountHeader(accountId: String): Result<AccountHeader> {
        return sessionManager.getAccountById(accountId)
    }

    fun login(accountId: String, pin: String) {
        val result = sessionManager.login(accountId, pin)

        result.onSuccess {
            loginResult.value =
                LoginResult(success = LoggedInUserView(account = it))
        }.onFailure {
            loginResult.value = LoginResult(error = it.message)
        }
    }

    fun loginDataChanged(accountId: String, password: String) {
        if (!isPasswordValid(password)) {
            loginFormState.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            loginFormState.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isPasswordValid(pin: String): Boolean {
        return pin.length == 4 && pin.isDigitsOnly()
    }
}