package de.app.ui.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import de.app.data.model.service.form.*
import de.app.databinding.ApplicationFormDateBinding
import de.app.databinding.FragmentAdministrativeServiceBinding
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class AdministrativeServiceFragment : Fragment() {

    private lateinit var viewModel: AdminServiceViewModel
    private lateinit var binding: FragmentAdministrativeServiceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdministrativeServiceBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AdminServiceViewModel::class.java]

        val formInflater = FormFieldInflater(inflater, binding.layout)
        viewModel.applicationForm.form.forEach {
            binding.layout.addView(convertFormFieldToView(it, formInflater))
        }


        binding.layout.addView(formInflater.inflateButton().root)
        return binding.root
    }

    private fun convertFormFieldToView(
        actual: FormField,
        inflater: FormFieldInflater
    ) = when (actual) {
        is InfoField -> inflater.inflateTextView().apply {
            root.text = actual.text
        }
        is DocumentField -> TODO()
        is ImageField -> TODO()
        is TextField -> inflater.inflateEditText().apply {
            label.text = actual.label
            field.hint = actual.hint
        }
        is BigTextField -> inflater.inflateEditTextBig().apply {
            label.text = actual.label
            field.hint = actual.hint
        }
        is EmailField -> TODO()
        is NumberField -> TODO()
        is SingleChoiceField -> TODO()
        is MultipleChoiceField -> TODO()
        is DateField -> inflater.inflateDate().apply {
            label.text = actual.label
            dateField.hint = actual.hint
            dateField.setOnFocusChangeListener { _, isFocused ->
                if(isFocused) {
                    showPicker()
                }
            }
            dateField.setOnClickListener { showPicker() }
        }
        is AttachmentField -> TODO()
    }.root

    private fun ApplicationFormDateBinding.showPicker() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .build()
        picker.addOnPositiveButtonClickListener {
            val selected = Instant.ofEpochMilli(it)
            dateField.setText(
                DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    .withZone(ZoneId.of("CET"))
                    .format(selected)
            )
        }

        picker
            .show(parentFragmentManager, "timePicker")
    }

}