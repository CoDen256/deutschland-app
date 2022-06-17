package de.app.ui.user.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.app.R
import de.app.api.account.CitizenServiceAccountRepository
import de.app.api.account.CompanyServiceAccountRepository
import javax.inject.Inject

class AccountRegisterViewModel @Inject constructor(
    private val citizenRepo: CitizenServiceAccountRepository,
    private val companyRepo: CompanyServiceAccountRepository,
) : ViewModel() {
    val formState = MutableLiveData<RegisterFormState>()
    val formResult = MutableLiveData<Result<RegisterUserView>>()

    enum class Type { CITIZEN, COMPANY }

    fun register(accountId: String, type: Type) {
        val secretToken = when (type) {
            Type.CITIZEN -> citizenRepo.getCitizenAccountSecretToken(accountId)
            Type.COMPANY -> companyRepo.getCompanyAccountSecretToken(accountId)
        }

        formResult.value = secretToken.map { RegisterUserView(it) }
    }

    fun accountIdChanged(accountId: String) {
        if (!isAccountIdValid(accountId)) {
            formState.value = RegisterFormState(accountIdError = R.string.invalid_account_id)
        } else {
            formState.value = RegisterFormState(isDataValid = true)
        }
    }

    fun accountTypeChanged(type: Type) {
        val text = when (type) {
            Type.CITIZEN -> R.string.enter_citizen_type
            Type.COMPANY -> R.string.enter_company_type
        }
        formState.value = RegisterFormState(
            formState.value?.accountIdError,
            text,
            formState.value?.isDataValid ?: false
        )
    }

    private fun isAccountIdValid(accountId: String): Boolean {
        return accountId.length >= 5
    }
}