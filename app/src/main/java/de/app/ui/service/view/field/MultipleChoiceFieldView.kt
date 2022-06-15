package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.ViewGroup
import de.app.api.service.form.MultipleChoiceField
import de.app.databinding.ApplicationFormMultipleChoiceBinding
import de.app.databinding.ApplicationFormMultipleChoiceItemBinding
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FieldValue

class MultipleChoiceFieldView(
    private val binding: ApplicationFormMultipleChoiceBinding,
    private val checkBoxes: List<ApplicationFormMultipleChoiceItemBinding>,
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
        val value = ArrayList<String>()
        checkBoxes.forEach {
            if (it.root.isChecked){
                value.add(it.root.text.toString())
            }
        }
        return FieldValue(id, value)
    }

    override fun setOnValueChangedListener(handler: () -> Unit) {
        checkBoxes.forEach {
            it.root.setOnCheckedChangeListener { _, _ ->
                handler()
            }
        }
    }

    class Inflater {
        private lateinit var binding: ApplicationFormMultipleChoiceBinding
        private lateinit var optionBinding:() -> ApplicationFormMultipleChoiceItemBinding
        private lateinit var id: String
        private val checkBoxes = ArrayList<ApplicationFormMultipleChoiceItemBinding>()

        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormMultipleChoiceBinding.inflate(inflater, parent, true)
            optionBinding = { ApplicationFormMultipleChoiceItemBinding.inflate(inflater, binding.fields, true) }
        }

        fun populate(field: MultipleChoiceField): Inflater = apply {
            binding.label.text = field.label

            field.options.forEach {
                val option = optionBinding()
                option.root.text = it
                checkBoxes.add(option)
            }

            id = field.id
        }

        fun build() = MultipleChoiceFieldView(binding, checkBoxes, id)
    }
}