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
import de.app.ui.service.data.result.FormResult
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.result.FormView
import de.app.ui.service.data.state.FieldState
import de.app.ui.service.verificator.Verificator

class AdminServiceViewModel : ViewModel() {

    private val registry: AdministrativeServiceRegistry = BaseAdministrativeServiceRegistry()
    private val service: AdministrativeService = registry.getAllServices()[0]
    val applicationForm: ApplicationForm = registry.getApplicationForm(service)

    val formState = MutableLiveData<FormState>()
    val result = MutableLiveData<FormResult>()

    fun submit(data: Map<String, Any>){

        val submittedForm = SubmittedForm(service, ArrayList<SubmittedField>().apply {
            data.forEach{
                add(SubmittedField(it.key, it.value))
            }
        })
        val rs = registry.sendApplicationForm(service, submittedForm)
        result.value = when(rs){
            is Result.Success -> FormResult(success = FormView())
            is Result.Error -> FormResult(error = rs.exception.message)
        }
    }

    fun formDataChanged(data: Map<String, Pair<Any, Verificator>>){
        val states = HashMap<String, FieldState>()
        for ((name, rest) in data) {
            val (value, verificator) = rest
            states[name] = verificator.verify(name, value)
        }
        formState.value = FormState(fieldStates = states,
            isDataValid = states.all { it.value.error == null })
    }

}