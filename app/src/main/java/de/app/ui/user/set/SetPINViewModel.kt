package de.app.ui.user.set

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.app.R
import de.app.api.account.CitizenServiceAccountRepository
import de.app.api.account.SecretToken
import de.app.core.SessionManager
import de.app.data.model.Address
import de.app.data.model.User
import de.app.data.model.UserHeader
import java.util.*
import javax.inject.Inject

class SetPINViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val citizenRepo: CitizenServiceAccountRepository,
    ) : ViewModel() {

    val setPINFormState = MutableLiveData<SetPINFormState>()
    val setPINResult = MutableLiveData<Result<SetPINUserView>>()

    fun setPIN(token: String, pin: String) {
        val account = citizenRepo.getCitizenAccount(SecretToken(token)).map {
            User(
                UUID.randomUUID().toString(),
                it.displayName,
                it.accountId,
                Address(it.city, it.country, it.postalCode),
                User.Type.CITIZEN
            )
        }.getOrThrow()
//        val result = sessionManager.addAccount(account, pin)
//        loginResult.value =
//            SetupResult(success = SetupUserView(account = account))
    }

    fun pinChanged(password: String) {
        if (!isPasswordValid(password)) {
            setPINFormState.value = SetPINFormState(passwordError = R.string.invalid_password)
        } else {
            setPINFormState.value = SetPINFormState(isDataValid = true)
        }
    }

    private fun isPasswordValid(pin: String): Boolean {
        return pin.length == 4 && pin.isDigitsOnly()
    }

    fun getAccountHeader(accountId: String): Result<UserHeader> {
        return null!!//sessionManager.getAccountById(accountId)
    }
}