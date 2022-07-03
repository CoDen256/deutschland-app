package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import de.app.R
import de.app.api.service.form.SingleChoiceField
import de.app.databinding.ApplicationFormSingleChoiceBinding
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FieldValue

class SingleChoiceFieldView(
    private val binding: ApplicationFormSingleChoiceBinding,
    private val id: String
) : InputFieldView {

    override fun applyState(formState: FormState) {
        formState.getFieldState(id)?.apply {
            if (error != null) {
                binding.label.error = error
            } else {
                binding.label.error = null
            }
        }
    }

    override fun getValue(): FieldValue {
        return FieldValue(id, binding.field.listSelection.toString())
    }

    override fun setOnValueChangedListener(handler: () -> Unit) {
        binding.field.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            { handler() }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    class Inflater {
        private lateinit var binding: ApplicationFormSingleChoiceBinding
        private lateinit var id: String

        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormSingleChoiceBinding.inflate(inflater, parent, true)
        }

        fun populate(field: SingleChoiceField, fragment: Fragment): Inflater = apply {
            binding.label.text = field.label
            binding.hint.hint = field.hint
            binding.field.setAdapter(ArrayAdapter(
                fragment.requireContext(),
                R.layout.application_form_single_choice_item,
                field.options
            ))
            id = field.id
        }

        fun build() = SingleChoiceFieldView(binding, id)
    }
}