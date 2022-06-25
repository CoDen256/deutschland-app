package de.app.ui.service

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.app.api.service.AdministrativeServiceRegistry
import de.app.core.config.BaseAdministrativeServiceRegistry
import de.app.api.service.AdministrativeService
import de.app.api.service.form.AttachmentField
import de.app.api.service.form.DocumentInfoField
import de.app.api.service.form.Form
import de.app.api.service.form.InputField
import de.app.api.service.submit.SubmittedField
import de.app.api.service.submit.SubmittedForm
import de.app.core.config.DataGenerator.Companion.generateDocuments
import de.app.data.model.FileHeader
import de.app.ui.service.data.result.FormResult
import de.app.ui.service.data.result.FormView
import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FormValue
import de.app.ui.service.validator.FieldValidator
import de.app.ui.service.validator.ValidatorProvider

class AdminServiceViewModel(id: String) : ViewModel() {

    private val registry: AdministrativeServiceRegistry = BaseAdministrativeServiceRegistry()
    val service: AdministrativeService = registry.getServiceById(id)
        .getOrThrow()

    val form: Form = Form(
        listOf(
            DocumentInfoField(label="Documnet", generateDocuments(5)),
            AttachmentField(id="at", required = true, "Attachment", "application/pdf")
        ),
        paymentRequired = true
    ) ?:registry.getApplicationForm(service).getOrThrow()

    val formState = MutableLiveData<FormState>()
    val result = MutableLiveData<FormResult>()

    private val validators = HashMap<String, FieldValidator>().apply {
        val provider = ValidatorProvider()
        form.fields.forEach {
            if (it is InputField) {
                put(it.id, provider.getValidator(it))
            }
        }
    }


    fun submit(data: FormValue) {
        val submittedForm = SubmittedForm(ArrayList<SubmittedField>().apply {
            data.values.forEach {
                add(SubmittedField(it.id, it.value))
            }
        })
        val rs = registry.sendApplicationForm(service, submittedForm)

        result.value = rs.fold({ FormResult(success = FormView()) }) {
            FormResult(error = it.message)
        }
    }


    fun formDataChanged(data: FormValue) {
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