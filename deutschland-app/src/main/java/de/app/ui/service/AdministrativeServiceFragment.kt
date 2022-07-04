package de.app.ui.service

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import de.app.databinding.FragmentAdministrativeServiceBinding
import de.app.ui.SubmittedResultActivity
import de.app.ui.components.AccountAwareFragment
import de.app.ui.safe.DataSafePickerFactory
import de.app.ui.service.data.result.FormView
import de.app.ui.service.data.value.FormValue
import de.app.ui.service.view.button.ButtonView
import de.app.ui.service.view.button.ButtonViewFactory
import de.app.ui.service.view.field.FieldView
import de.app.ui.service.view.field.FieldViewFactory
import de.app.ui.service.view.field.InputFieldView
import de.app.ui.util.observe
import de.app.ui.util.openUrl
import de.app.ui.util.runActivity
import de.app.ui.util.toast
import javax.inject.Inject

@AndroidEntryPoint
class AdministrativeServiceFragment : AccountAwareFragment<FragmentAdministrativeServiceBinding>() {
    private val args: AdministrativeServiceFragmentArgs by navArgs()

    @Inject lateinit var factory: AdminServiceViewModelFactory
    @Inject lateinit var pickerFactory: DataSafePickerFactory
    private lateinit var viewModel: AdminServiceViewModel
    private lateinit var inflater: LayoutInflater
    private lateinit var inputFields: List<InputFieldView>
    private lateinit var submitButtonView: ButtonView

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAdministrativeServiceBinding.inflate(inflater, container, false).also {
        this.inflater = inflater
    }

    override fun setup() {
        viewModel = factory.getModelForServiceId(account, args.id).getOrElse {
            requireActivity().toast("Failed getting view model for service: ${it.message}")
            return
        }

        val root = binding.layout

        binding.serviceName.text = viewModel.getServiceName()
        binding.serviceDescription.text = viewModel.getServiceDescription()

        // Inflate fields
        val fields = inflateFields(this, inflater, root)
        inputFields = fields.filterIsInstance<InputFieldView>()

        // Inflate submit button
        submitButtonView = inflateSubmitButton(inflater, root)

        prefillInputFields()
        observeInputFields()
        observeSubmitButton()

        observeFormState()
        observeResult()
    }

    private fun prefillInputFields() {
        val prefiller = Prefiller()
        inputFields.forEach { field ->
            field.prefill { prefiller.prefill(it, account) }
        }
    }

    private fun inflateFields(
        fragment: Fragment,
        inflater: LayoutInflater,
        parent: ViewGroup
    ): List<FieldView> {
        val factory = FieldViewFactory(fragment, inflater, parent, pickerFactory.createShower(requireActivity(), account))
        return viewModel.getFields().map { factory.createFieldView(it) }
    }

    private fun inflateSubmitButton(inflater: LayoutInflater, parent: ViewGroup): ButtonView {
        return ButtonViewFactory(inflater, parent).createSubmitButtonView()
    }

    private fun observeInputFields() {
        inputFields.forEach { field ->
            field.setOnValueChangedListener {
                viewModel.formDataChanged(FormValue(collectValues()))
            }
        }
    }

    private fun observeSubmitButton() {
        submitButtonView.setOnClickListener {
            viewModel.submit(FormValue(collectValues()))
        }
    }

    private fun collectValues() = HashSet(inputFields.map { it.getValue() })

    private fun observeFormState() {
        observe(viewModel.formState) {
            submitButtonView.applyState(this)
            inputFields.forEach { it.applyState(this) }
        }
    }

    private fun observeResult() {
        observe(viewModel.result, {onSuccess(it)}, {onError(it)})
    }

    private fun onSuccess(formView: FormView) {
        formView.uri.onSuccess {
            requireContext().openUrl(it)
        }
        formView.bundle.onSuccess {
            requireActivity().runActivity(SubmittedResultActivity::class.java, it)
        }
    }

    private fun onError(throwable: Throwable) {
        requireActivity().toast("Error: "+throwable.message)
    }

}