package de.app.ui.account.setup

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.app.R
import de.app.core.SessionManager
import de.app.data.Result
import de.app.ui.account.login.data.LoggedInUserView
import de.app.ui.account.login.data.LoginFormState
import de.app.ui.account.login.data.LoginResult

class AccountSetPINViewModel(private val sessionManager: SessionManager) : ViewModel() {

    val loginFormState = MutableLiveData<LoginFormState>()
    val loginResult = MutableLiveData<LoginResult>()

    fun login(accountId: String, pin: String) {
        val result = sessionManager.login(accountId, pin)

        if (result is Result.Success) {
            loginResult.value =
                LoginResult(success = LoggedInUserView(account = result.data))
        } else if (result is Result.Error) {
            loginResult.value = LoginResult(error = result.exception.message)
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