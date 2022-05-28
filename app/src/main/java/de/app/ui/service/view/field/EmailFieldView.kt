package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.app.data.model.service.form.EmailField
import de.app.databinding.ApplicationFormEmailBinding
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FieldValue
import de.app.ui.util.afterTextChanged

class EmailFieldView(
    private val binding: ApplicationFormEmailBinding,
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

    override fun onValueChanged(handler: () -> Unit) {
        binding.field.afterTextChanged {
            handler()
        }
    }

    override fun getView(): View {
        return binding.root
    }

    class Inflater {
        private lateinit var binding: ApplicationFormEmailBinding
        private lateinit var id: String

        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormEmailBinding.inflate(inflater, parent, false)
        }

        fun populate(field: EmailField): Inflater = apply {
            binding.label.text = field.label
            binding.field.hint = field.hint
            id = field.id
        }

        fun build() = EmailFieldView(binding, id)
    }
}