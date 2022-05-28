package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.app.data.model.service.form.DateField
import de.app.databinding.ApplicationFormDateBinding
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FieldValue
import de.app.ui.util.afterTextChanged
import de.app.ui.util.showPicker

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

    override fun onValueChanged(handler: () -> Unit) {
        binding.field.afterTextChanged {
            handler()
        }
    }

    override fun getView(): View {
        return binding.root
    }

    class Inflater {
        private lateinit var binding: ApplicationFormDateBinding
        private lateinit var id: String

        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormDateBinding.inflate(inflater, parent, false)
        }

        fun populate(field: DateField, fragment: Fragment): Inflater = apply {
            binding.label.text = field.label
            binding.field.apply {
                hint = field.hint
                setOnFocusChangeListener { _, isFocused ->
                    if (isFocused) showPicker(fragment.parentFragmentManager)
                }
                setOnClickListener { showPicker(fragment.parentFragmentManager) }
            }
            id = field.id
        }

        fun build(): DateFieldView = DateFieldView(binding, id)
    }
}