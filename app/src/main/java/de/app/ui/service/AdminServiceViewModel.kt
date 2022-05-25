package de.app.ui.service

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.app.api.AdministrativeServiceRegistry
import de.app.data.model.service.AdministrativeService
import de.app.data.model.service.form.ApplicationForm
import de.app.api.dummy.BaseAdministrativeServiceRegistry
import de.app.data.Result
import de.app.data.model.service.submit.SubmittedField
import de.app.data.model.service.submit.SubmittedForm
import de.app.ui.account.login.data.LoginFormState
import de.app.ui.account.login.data.LoginResult
import de.app.ui.service.data.FormResult
import de.app.ui.service.data.FormState
import de.app.ui.service.data.FormView

class AdminServiceViewModel : ViewModel() {

    private val registry: AdministrativeServiceRegistry = BaseAdministrativeServiceRegistry()
    private val service: AdministrativeService = registry.getAllServices()[0]
    val applicationForm: ApplicationForm = registry.getApplicationForm(service)

    val formState = MutableLiveData<FormState>()
    val result = MutableLiveData<FormResult>()

    fun submit(name: String, surname: String, birthday: String){
        val submittedForm = SubmittedForm(service, listOf(
            SubmittedField("name", name),
            SubmittedField("surname", surname),
            SubmittedField("birthday", birthday)
        ))
        val rs = registry.sendApplicationForm(service, submittedForm)
        result.value = when(rs){
            is Result.Success -> FormResult(success = FormView())
            is Result.Error -> FormResult(error = rs.exception.message)
        }
    }

    fun formDataChanged(name: String, surname: String, birthday: String){
        if (birthday.endsWith("2022")){
            formState.value = FormState(birthdayError = "Can't be 2022")
        }else {
            formState.value = FormState(isDataValid = true)
        }
    }

}