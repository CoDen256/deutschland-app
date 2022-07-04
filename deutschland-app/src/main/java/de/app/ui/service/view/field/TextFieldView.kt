package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.ViewGroup
import de.app.api.service.form.TextField
import de.app.databinding.ApplicationFormTextBinding
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FieldValue
import de.app.ui.util.afterTextChanged
import de.app.ui.util.editable

class TextFieldView(
    private val binding: ApplicationFormTextBinding,
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
        return FieldValue(id, binding.field.text.toString())
    }

    override fun setOnValueChangedListener(handler: () -> Unit) {
        binding.field.afterTextChanged {
            handler()
        }
    }

    override fun prefill(prefiller: (String) -> String?) {
        prefiller(id)?.let {
            binding.field.text = it.editable()
        }
    }
    class Inflater {
        private lateinit var binding: ApplicationFormTextBinding
        private lateinit var id: String

        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormTextBinding.inflate(inflater, parent, true)
        }

        fun populate(field: TextField): Inflater = apply {
            binding.label.text = field.label
            binding.field.hint = field.hint
            id = field.id
        }

        fun build() = TextFieldView(binding, id)
    }
}