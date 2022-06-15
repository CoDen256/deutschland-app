package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import de.app.api.service.form.RadioChoiceField
import de.app.databinding.ApplicationFormRadioChoiceBinding
import de.app.databinding.ApplicationFormRadioChoiceItemBinding
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FieldValue

class RadioChoiceFieldView(
    private val binding: ApplicationFormRadioChoiceBinding,
    private val id: String
) : InputFieldView {

    private val buttons: MutableMap<Int, RadioButton> = HashMap()

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
        val value: String? = when {
            binding.fields.checkedRadioButtonId >= 0 -> {
                binding.fields.let {
                    buttons.getOrPut(it.checkedRadioButtonId) { it.findViewById(it.checkedRadioButtonId) }
                }.text.toString()
            }
            else -> null
        }
        return FieldValue(id, value)
    }

    override fun setOnValueChangedListener(handler: () -> Unit) {
        binding.fields.setOnCheckedChangeListener { _, _ -> handler() }
    }

    class Inflater {
        private lateinit var binding: ApplicationFormRadioChoiceBinding
        private lateinit var optionBinding:() -> ApplicationFormRadioChoiceItemBinding
        private lateinit var id: String

        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormRadioChoiceBinding.inflate(inflater, parent, true)
            optionBinding = { ApplicationFormRadioChoiceItemBinding.inflate(inflater, binding.fields, true) }
        }

        fun populate(field: RadioChoiceField): Inflater = apply {
            binding.label.text = field.label

            field.options.forEach {
                val option = optionBinding()
                option.root.text = it
            }

            id = field.id
        }

        fun build() = RadioChoiceFieldView(binding, id)
    }
}