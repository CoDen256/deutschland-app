package de.app.ui.service

import android.net.Uri
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.app.api.account.ServiceAccount
import de.app.api.service.AdministrativeServiceRegistry
import de.app.api.service.AdministrativeService
import de.app.api.service.form.Field
import de.app.api.service.form.Form
import de.app.api.service.form.InputField
import de.app.api.service.submit.SubmittedField
import de.app.api.service.submit.SubmittedForm
import de.app.core.flatMap
import de.app.core.success
import de.app.ui.service.data.result.FormView
import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FormValue
import de.app.ui.service.validator.FieldValidator
import de.app.ui.service.validator.ValidatorProvider
import java.lang.AssertionError
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class AdminServiceViewModel internal constructor(
    private val registry: AdministrativeServiceRegistry,
    private val account: ServiceAccount,
    private val service: AdministrativeService,
    private val form: Form
) : ViewModel() {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss dd MMMM yy")

    private val validators = HashMap<String, FieldValidator>().apply {
        val provider = ValidatorProvider()
        form.fields.forEach {
            if (it is InputField) {
                put(it.id, provider.getValidator(it))
            }
        }
    }

    val formState = MutableLiveData<FormState>()
    val result = MutableLiveData<Result<FormView>>()

    fun getServiceName(): String{
        return service.name
    }

    fun getServiceDescription(): String{
        return service.description
    }

    fun getFields(): List<Field> {
        return form.fields
    }

    fun submit(data: FormValue) {
        val submittedForm = SubmittedForm(ArrayList<SubmittedField>().apply {
            data.values.forEach {
                add(SubmittedField(it.id, it.value))
            }
        })
        val rs = registry.sendApplicationForm(account, service, submittedForm)

        result.value = rs.map {
            when {
                form.paymentRequired -> formViewForPayment()
                else -> formViewWithoutPayment()
            }
        }
    }

    private fun formViewWithoutPayment() = FormView(
        Result.failure(AssertionError("You should've unpacked Bundle")),
        buildBundleFromParams(buildFormViewMap()).success()
    )

    private fun formViewForPayment() = FormView(
        buildPaymentRequiredUriFromParams(buildFormViewMap()).success(),
        Result.failure(AssertionError("You should've unpacked Uri"))
    )


    private fun buildBundleFromParams(params: Map<String, String>): Bundle {
        return bundleOf(
            *params.map { it.key to it.value }.toTypedArray()
        )
    }

    private fun buildPaymentRequiredUriFromParams(params: Map<String, String>): Uri {
        return Uri.Builder()
            .scheme("https")
            .authority("coden256.github.io")
            .path("/deutschland-app/")
            .apply {
                for ((name, value) in params)
                    appendQueryParameter(name, value)
            }.build()
    }

    private fun buildFormViewMap(): Map<String, String> {
        return mapOf(
            "applicationId" to UUID.randomUUID().toString().substring(0, 6),
            "accountDisplayName" to account.displayName,
            "accountId" to account.accountId,
            "serviceName" to service.name,
            "sentDate" to LocalDateTime.now().format(dateTimeFormatter)
        )
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

@Singleton
class AdminServiceViewModelFactory @Inject constructor(private val registry: AdministrativeServiceRegistry) {

    fun getModelForServiceId(account: ServiceAccount, serviceId: String): Result<AdminServiceViewModel> =
        registry.getServiceById(serviceId).flatMap { service ->
            registry.getApplicationForm(service).map { service to it }
        }.map {
            val (service, form) = it
            AdminServiceViewModel(registry, account, service, form)
        }
}