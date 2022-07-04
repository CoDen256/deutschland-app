package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.app.api.service.form.DateField
import de.app.databinding.ApplicationFormDateBinding
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FieldValue
import de.app.ui.util.afterTextChanged
import de.app.ui.util.editable
import de.app.ui.util.showDatePicker

class DateFieldView(
    private val binding: ApplicationFormDateBinding,
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

    override fun prefill(prefiller: (String) -> String?) {
        prefiller(id)?.let {
            binding.field.text = it.editable()
        }
    }
    class Inflater {
        private lateinit var binding: ApplicationFormDateBinding
        private lateinit var id: String

        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormDateBinding.inflate(inflater, parent, true)
        }

        fun populate(field: DateField, fragment: Fragment): Inflater = apply {
            binding.label.text = field.label
            binding.field.apply {
                hint = field.hint
                setOnFocusChangeListener { _, isFocused ->
                    if (isFocused) showDatePicker(fragment.parentFragmentManager)
                }
                setOnClickListener { showDatePicker(fragment.parentFragmentManager) }
            }
            id = field.id
        }

        fun build(): DateFieldView = DateFieldView(binding, id)
    }
}