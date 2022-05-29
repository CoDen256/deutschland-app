package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.ViewGroup
import de.app.data.model.service.form.NumberField
import de.app.databinding.ApplicationFormNumberBinding
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FieldValue
import de.app.ui.util.afterTextChanged

class NumberFieldView(
    private val binding: ApplicationFormNumberBinding,
    private val id: String
) : InputFieldView {

    override fun applyState(formState: FormState) {
        formState.getFieldState(id)?.apply {
            if (error != null){
                binding.field.error = error
            }else{
                binding.field.error = null
            }
        }
    }

    override fun getValue(): FieldValue {
        return FieldValue(id, binding.field.text.toString().toIntOrNull())
    }

    override fun setOnValueChangedListener(handler: () -> Unit) {
        binding.field.afterTextChanged {
            handler()
        }
    }

    class Inflater {
        private lateinit var binding: ApplicationFormNumberBinding
        private lateinit var id: String

        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormNumberBinding.inflate(inflater, parent, true)
        }

        fun populate(field: NumberField): Inflater = apply {
            binding.label.text = field.label
            binding.field.hint = field.hint
            id = field.id
        }

        fun build() = NumberFieldView(binding, id)
    }
}