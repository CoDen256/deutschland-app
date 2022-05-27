package de.app.ui.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import de.app.databinding.FragmentAdministrativeServiceBinding
import de.app.ui.service.data.value.FormValue
import de.app.ui.service.inflater.FieldInflater
import de.app.ui.service.view.FieldView
import de.app.ui.service.view.InputFieldView

class AdministrativeServiceFragment : Fragment() {

    private lateinit var viewModel: AdminServiceViewModel
    private lateinit var binding: FragmentAdministrativeServiceBinding

    private val fields = HashMap<String, InputFieldView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdministrativeServiceBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AdminServiceViewModel::class.java]

        val formInflater = FieldInflater(inflater, binding.layout)

        inflateFields(formInflater)
        val submitButton = inflateSubmitButton(formInflater)

        observeFormState(submitButton)
        observeResult()

        return binding.root
    }

    private fun inflateFields(fieldInflater: FieldInflater) {
        viewModel.form.fields.forEach { field ->
            val view: FieldView = fieldInflater.convertFormFieldToView(field, this)
            if (view is InputFieldView){
                view.onValueChanged {
                    viewModel.formDataChanged(
                        FormValue(HashSet(fields.values.map { it.getCurrentValue() }))
                    )
                }
                fields.put("", view)
            }
            binding.layout.addView(view.view)
        }
    }

    private fun inflateSubmitButton(formInflater: FieldInflater): Button {
        val submitButton = formInflater.inflateButton().root.apply {
            setOnClickListener {
                viewModel.submit(
                    FormValue(HashSet(fields.values.map { it.getCurrentValue() }))
                )
            }
        }
        binding.layout.addView(submitButton)
        return submitButton
    }

    private fun observeFormState(submitButton: Button) {
        viewModel.formState.observe(viewLifecycleOwner, Observer { state ->
            val formState = state ?: return@Observer
            submitButton.isEnabled = formState.isDataValid

            formState.states.forEach {
                fields[it.id]?.applyState(it)
            }
        })
    }

    private fun observeResult() {
        viewModel.result.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer

            if (result.success != null) {
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
            }
            if (result.error != null) {
                Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
            }
        })
    }


}