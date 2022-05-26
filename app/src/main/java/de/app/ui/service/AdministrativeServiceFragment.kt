package de.app.ui.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import de.app.data.model.service.form.*
import de.app.databinding.FragmentAdministrativeServiceBinding
import de.app.ui.service.data.value.FormValue
import de.app.ui.service.inflater.FieldInflater
import de.app.ui.service.verificator.DateFieldValidator
import de.app.ui.service.verificator.TextFieldValidator
import de.app.ui.service.verificator.FieldValidator
import de.app.ui.service.view.FieldView
import de.app.ui.service.view.EditTextView
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AdministrativeServiceFragment : Fragment() {

    private lateinit var viewModel: AdminServiceViewModel
    private lateinit var binding: FragmentAdministrativeServiceBinding

    private val fields = HashMap<String, FieldView>()
    private val validators = HashMap<String, FieldValidator>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdministrativeServiceBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AdminServiceViewModel::class.java]

        val formInflater = FieldInflater(inflater, binding.layout)
        inflateForm(formInflater)


        fields.values.forEach { view ->
            view.onValueChanged {
                viewModel.formDataChanged(
                    FormValue(HashSet(fields.values.map { it.getValue()})), validators)
            }
        }

        val submitButton = formInflater.inflateButton().root.apply {
            setOnClickListener {
                viewModel.submit(
                    FormValue(HashSet(fields.values.map { it.getValue()}))
                )
            }
        }
        binding.layout.addView(submitButton)

        viewModel.formState.observe(viewLifecycleOwner, Observer { state ->
            val formState = state ?: return@Observer
            submitButton.isEnabled = formState.isDataValid

            formState.states.forEach {
                fields[it.id]?.applyState(it)
            }
        })

        viewModel.result.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer

            if (result.success != null) {
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
            }
            if (result.error != null) {
                Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    private fun inflateForm(formInflater: FieldInflater) {
        viewModel.form.form.forEach {
            val view = convertFormFieldToView(it, formInflater)
            binding.layout.addView(view)
        }
    }

    private fun convertFormFieldToView(
        actual: Field,
        inflater: FieldInflater
    ) = when (actual) {
        is InfoField -> inflater.inflateTextView().apply {
            root.text = actual.text
        }
        is DocumentField -> TODO()
        is ImageField -> TODO()
        is TextField -> inflater.inflateEditText().apply {
            label.text = actual.label
            field.hint = actual.hint
            fields[actual.name] = EditTextView(actual.name, field)
            validators[actual.name] = TextFieldValidator()
        }
        is BigTextField -> inflater.inflateEditTextBig().apply {
            label.text = actual.label
            field.hint = actual.hint
            fields[actual.name] = EditTextView(actual.name, field)
            validators[actual.name] = TextFieldValidator()
        }
        is EmailField -> TODO()
        is NumberField -> TODO()
        is SingleChoiceField -> TODO()
        is MultipleChoiceField -> TODO()
        is DateField -> inflater.inflateDate().apply {
            label.text = actual.label
            dateField.hint = actual.hint
            dateField.setOnFocusChangeListener { _, isFocused ->
                if (isFocused) dateField.showPicker()
            }
            dateField.setOnClickListener { dateField.showPicker() }
            fields[actual.name] = EditTextView(actual.name, dateField)
            validators[actual.name] = DateFieldValidator()
        }
        is AttachmentField -> TODO()
    }.root

    private fun TextView.showPicker() {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    text = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                        .withZone(ZoneId.of("CET"))
                        .format(Instant.ofEpochMilli(it))
                }
            }
            .show(parentFragmentManager, "datePicker")
    }

}