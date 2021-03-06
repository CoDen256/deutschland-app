package de.app.ui.user.set

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.app.R
import de.app.api.account.ServiceAccount
import de.app.api.account.CitizenServiceAccountRepository
import de.app.api.account.CompanyServiceAccountRepository
import de.app.api.account.SecretToken
import de.app.core.SessionManager
import de.app.data.model.User
import de.app.data.model.UserType
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Set p i n view model
 *
 * @property sessionManager
 * @property citizenRepo
 * @property companyRepo
 * @constructor Create empty Set p i n view model
 */
class SetPINViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val citizenRepo: CitizenServiceAccountRepository,
    private val companyRepo: CompanyServiceAccountRepository,
) : ViewModel() {

    val setPINFormState = MutableLiveData<SetPINFormState>()
    val setPINResult = MutableLiveData<Result<SetPINView>>()


    /**
     * Set p i n
     *
     * @param tokenА
     * @param type
     * @param pin
     */
    fun setPIN(token: String, type: UserType, pin: String) {
        pinChanged(pin)
        if (setPINFormState.value?.isDataValid != true){
            return
        }
        viewModelScope.launch {
            val secretToken = SecretToken(token)
            val user: Result<User> = when (type) {
                UserType.CITIZEN -> createUser(citizenRepo.getCitizenAccount(secretToken), token, type)
                UserType.COMPANY -> createUser(companyRepo.getCompanyAccount(secretToken), token, type)
            }
            setPINResult.value = user.map {
                sessionManager.addAccount(it, pin)
                SetPINView(it)
            }
        }
    }

    private fun createUser(account: Result<ServiceAccount>, token: String, type: UserType): Result<User> {
        return account.map {
            User(
                UUID.randomUUID().toString(),
                it.displayName,
                token,
                it.address,
                type,
            )
        }

    }

    /**
     * Pin changed
     *
     * @param password
     */
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
}