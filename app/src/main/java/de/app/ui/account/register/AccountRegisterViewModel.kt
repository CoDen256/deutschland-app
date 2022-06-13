package de.app.ui.account.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.app.R
import de.app.api.CitizenServiceAccountRepository
import de.app.api.CompanyServiceAccountRepository
import de.app.core.SessionManager
import de.app.data.Result
import de.app.ui.account.login.data.LoggedInUserView
import de.app.ui.account.login.data.LoginResult
import de.app.ui.account.register.data.RegisterFormState
import javax.inject.Inject

class AccountRegisterViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val citizenRepo: CitizenServiceAccountRepository,
    private val companyRepo: CompanyServiceAccountRepository,
    ) : ViewModel() {
    val formState = MutableLiveData<RegisterFormState>()
    val formResult = MutableLiveData<LoginResult>()

    enum class Type {
        CITIZEN, COMPANY
    }

    fun register(accountId: String, type: Type) {
        val result = when (type) {
            Type.CITIZEN -> citizenRepo.getCitizenAccount(accountId)
            Type.COMPANY -> companyRepo.getCompanyAccount(accountId)
        }

        if (result is Result.Success) {
            formResult.value =
                LoginResult(success = LoggedInUserView(account = result.data))
        } else if (result is Result.Error) {
            formResult.value = LoginResult(error = result.exception.message)
        }
    }

    fun accountIdChanged(accountId: String) {
        if (!isAccountIdValid(accountId)) {
            formState.value = RegisterFormState(accountIdError = R.string.invalid_account_id)
        } else {
            formState.value = RegisterFormState(isDataValid = true)
        }
    }

    fun accountTypeChanged(type: Type){
        val text = when(type){
            Type.CITIZEN -> R.string.enter_citizen_type
            Type.COMPANY -> R.string.enter_company_type
        }
        formState.value = RegisterFormState(formState.value?.accountIdError, text, formState.value?.isDataValid ?: false)
    }

    private fun isAccountIdValid(accountId: String): Boolean {
        return accountId.length > 4
    }
}