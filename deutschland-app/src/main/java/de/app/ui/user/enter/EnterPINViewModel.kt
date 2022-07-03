package de.app.ui.user.enter

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.app.R
import de.app.core.SessionManager
import de.app.data.model.UserHeader
import kotlinx.coroutines.launch
import javax.inject.Inject

class EnterPINViewModel @Inject constructor(private val sessionManager: SessionManager) : ViewModel() {

    val loginFormState = MutableLiveData<EnterPINFormState>()
    val loginResult = MutableLiveData<Result<EnterPINView>>()

    suspend fun getUserHeader(userId: String): Result<UserHeader> {
        return sessionManager.getUserById(userId)
    }

    fun login(userId: String, pin: String) {
        loginDataChanged(pin)
        if (loginFormState.value?.isDataValid != true){
            return
        }
        viewModelScope.launch {
            val result = sessionManager.login(userId, pin)

            loginResult.value = result.map { EnterPINView() }
        }
    }

    fun loginDataChanged(password: String) {
        if (!isPasswordValid(password)) {
            loginFormState.value = EnterPINFormState(passwordError = R.string.invalid_password)
        } else {
            loginFormState.value = EnterPINFormState(isDataValid = true)
        }
    }

    private fun isPasswordValid(pin: String): Boolean {
        return pin.length == 4 && pin.isDigitsOnly()
    }
}