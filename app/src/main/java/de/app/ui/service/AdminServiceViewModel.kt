package de.app.ui.service

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.app.api.AdministrativeServiceRegistry
import de.app.api.dummy.BaseAdministrativeServiceRegistry
import de.app.data.Result
import de.app.data.model.service.AdministrativeService
import de.app.data.model.service.form.Form
import de.app.data.model.service.form.InputField
import de.app.data.model.service.submit.SubmittedField
import de.app.data.model.service.submit.SubmittedForm
import de.app.ui.service.data.result.FormResult
import de.app.ui.service.data.result.FormView
import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FormValue
import de.app.ui.service.validator.FieldValidator

class AdminServiceViewModel : ViewModel() {

    private val registry: AdministrativeServiceRegistry = BaseAdministrativeServiceRegistry()
    private val service: AdministrativeService = registry.getAllServices()[0]

    val form: Form = registry.getApplicationForm(service)

    val formState = MutableLiveData<FormState>()
    val result = MutableLiveData<FormResult>()

    private val validators = HashMap<String, FieldValidator>().apply {
        val provider = ValidatorProvider()
        form.fields.forEach {
            if (it is InputField){
                put(it.id, provider.getValidator(it))
            }
        }
    }


    fun submit(data: FormValue){
        val submittedForm = SubmittedForm(ArrayList<SubmittedField>().apply {
            data.values.forEach{
                add(SubmittedField(it.id, it.value))
            }
        })
        val rs = registry.sendApplicationForm(service, submittedForm)
        result.value = when(rs){
            is Result.Success -> FormResult(success = FormView())
            is Result.Error -> FormResult(error = rs.exception.message)
        }
    }

    fun formDataChanged(data: FormValue){
        val states = HashSet<FieldState>()
        for (value in data.values) {
            states.add(validators[value.id]!!.validate(value))
        }
        formState.value = FormState(
            states = states,
            isDataValid = states.all { it.error == null }
        )
    }

}