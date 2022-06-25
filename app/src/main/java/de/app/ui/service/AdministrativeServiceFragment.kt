package de.app.ui.service

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import de.app.databinding.FragmentAdministrativeServiceBinding
import de.app.ui.SubmittedResultActivity
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

class AdministrativeServiceFragment : Fragment() {

    private lateinit var viewModel: AdminServiceViewModel
    private lateinit var binding: FragmentAdministrativeServiceBinding

    private lateinit var inputFields: List<InputFieldView>
    private lateinit var submitButtonView: ButtonView
    private val args: AdministrativeServiceFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdministrativeServiceBinding.inflate(inflater, container, false)
        viewModel = AdminServiceViewModel(args.id)
        val root = binding.layout


        binding.serviceName.text = viewModel.service.name
        binding.serviceDescription.text = viewModel.service.description

        // Inflate fields
        val fields = inflateFields(this, inflater, root)
        inputFields = fields.filterIsInstance<InputFieldView>()

        // Inflate submit button
        submitButtonView = inflateSubmitButton(inflater, root)

        observeInputFields()
        observeSubmitButton()

        observeFormState()
        observeResult()

        return binding.root
    }

    private fun inflateFields(
        fragment: Fragment,
        inflater: LayoutInflater,
        parent: ViewGroup
    ): List<FieldView> {
        val factory = FieldViewFactory(fragment, inflater, parent)
        return viewModel.form.fields.map { factory.createFieldView(it) }
    }

    private fun inflateSubmitButton(inflater: LayoutInflater, parent: ViewGroup): ButtonView {
        return ButtonViewFactory(inflater, parent).createSubmitButtonView()
    }

    private fun observeInputFields() {
        inputFields.forEach { field ->
            field.setOnValueChangedListener {
                viewModel.formDataChanged(
                    FormValue(HashSet(inputFields.map { it.getValue() }))
                )
            }
        }
    }

    private fun observeSubmitButton() {
        submitButtonView.setOnClickListener {
            viewModel.submit(
                FormValue(HashSet(inputFields.map { it.getValue() }))
            )
        }
    }

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
        if (viewModel.form.paymentRequired){
            val uri = Uri.Builder()
                .scheme("https")
                .authority("coden256.github.io")
                .path("/deutschland-app/")
                .appendQueryParameter("applicationId",formView.applicationId)
                .appendQueryParameter("accountDisplayName", formView.accountDisplayName)
                .appendQueryParameter("accountId", formView.accountId)
                .appendQueryParameter("serviceName", formView.serviceName)
                .appendQueryParameter("sentDate", formView.sentDate)
                .build()
            requireContext().openUrl(uri)
        }else{
            requireActivity().runActivity(SubmittedResultActivity::class.java)
        }
    }

    private fun onError(throwable: Throwable) {
        Toast.makeText(requireContext(), throwable.message, Toast.LENGTH_SHORT).show()
    }
}