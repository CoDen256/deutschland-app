package de.app.ui.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.app.data.model.service.form.*
import de.app.databinding.FragmentAdministrativeServiceBinding

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
        is DateField -> TODO()
        is AttachmentField -> TODO()
    }.root

}