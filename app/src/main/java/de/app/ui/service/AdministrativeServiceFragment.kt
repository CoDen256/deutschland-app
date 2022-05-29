package de.app.ui.service

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import de.app.databinding.FragmentAdministrativeServiceBinding
import de.app.ui.service.data.result.FormResult
import de.app.ui.service.data.value.FormValue
import de.app.ui.service.view.button.ButtonView
import de.app.ui.service.view.button.ButtonViewFactory
import de.app.ui.service.view.field.FieldView
import de.app.ui.service.view.field.FieldViewFactory
import de.app.ui.service.view.field.InputFieldView

class AdministrativeServiceFragment : Fragment() {

    private lateinit var viewModel: AdminServiceViewModel
    private lateinit var binding: FragmentAdministrativeServiceBinding

    private lateinit var inputFields: List<InputFieldView>
    private lateinit var submitButtonView: ButtonView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdministrativeServiceBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AdminServiceViewModel::class.java]
        val root = binding.layout


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

    private fun inflateFields(fragment: Fragment, inflater: LayoutInflater, parent: ViewGroup): List<FieldView> {
        val factory = FieldViewFactory(fragment, inflater, parent)
        return viewModel.form.fields.map { factory.createFieldView(it) }
    }

    private fun inflateSubmitButton(inflater: LayoutInflater, parent: ViewGroup): ButtonView {
        return ButtonViewFactory(inflater, parent).createSubmitButtonView()
    }

    private fun observeInputFields() {
        inputFields.forEach { field ->
            field.onValueChanged {
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
        viewModel.formState.observe(viewLifecycleOwner, Observer { newState ->
            val formState = newState ?: return@Observer

            submitButtonView.applyState(formState)
            inputFields.forEach { it.applyState(formState) }
        })
    }

    private fun observeResult() {
        viewModel.result.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer

            if (result.success != null) {
                onSuccess()
            }
            if (result.error != null) {
                onError(result)
            }
            viewModel.result.value = null
        })
    }

    private fun onSuccess() {
        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
    }

    private fun onError(result: FormResult) {
        Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
    }
}