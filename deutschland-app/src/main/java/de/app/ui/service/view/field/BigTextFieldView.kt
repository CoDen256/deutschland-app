package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.ViewGroup
import de.app.api.service.form.BigTextField
import de.app.databinding.ApplicationFormBigTextBinding
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FieldValue
import de.app.ui.util.afterTextChanged

class BigTextFieldView(
    private val binding: ApplicationFormBigTextBinding,
    private val id: String
) : InputFieldView {

    override fun applyState(formState: FormState) {
        formState.getFieldState(id)?.apply {
            if (error != null) {
                binding.field.error = error
            } else {
                binding.field.error = null
            }
        }
    }

    override fun getValue(): FieldValue {
        return FieldValue(id, binding.field.text.toString())
    }

    override fun setOnValueChangedListener(handler: () -> Unit) {
        binding.field.afterTextChanged {
            handler()
        }
    }

    class Inflater {
        private lateinit var binding: ApplicationFormBigTextBinding
        private lateinit var id: String

        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormBigTextBinding.inflate(inflater, parent, true)
        }

        fun populate(field: BigTextField): Inflater = apply {
            binding.label.text = field.label
            binding.field.hint = field.hint
            id = field.id
        }

        fun build() = BigTextFieldView(binding, id)
    }
}