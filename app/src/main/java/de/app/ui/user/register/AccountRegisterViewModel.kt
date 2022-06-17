package de.app.ui.user.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.app.R
import de.app.api.account.CitizenServiceAccountRepository
import de.app.api.account.CompanyServiceAccountRepository
import de.app.data.model.UserType
import javax.inject.Inject

class AccountRegisterViewModel @Inject constructor(
    private val citizenRepo: CitizenServiceAccountRepository,
    private val companyRepo: CompanyServiceAccountRepository,
) : ViewModel() {
    val formState = MutableLiveData<RegisterFormState>()
    val formResult = MutableLiveData<Result<RegisterView>>()


    fun register(accountId: String, type: UserType) {
        val secretToken = when (type) {
            UserType.CITIZEN -> citizenRepo.getCitizenAccountSecretToken(accountId)
            UserType.COMPANY -> companyRepo.getCompanyAccountSecretToken(accountId)
        }

        formResult.value = secretToken.map { RegisterView(it, type) }
    }

    fun accountIdChanged(accountId: String) {
        if (!isAccountIdValid(accountId)) {
            formState.value = RegisterFormState(accountIdError = R.string.invalid_account_id)
        } else {
            formState.value = RegisterFormState(isDataValid = true)
        }
    }

    fun accountTypeChanged(type: UserType) {
        val text = when (type) {
            UserType.CITIZEN -> R.string.enter_citizen_type
            UserType.COMPANY -> R.string.enter_company_type
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