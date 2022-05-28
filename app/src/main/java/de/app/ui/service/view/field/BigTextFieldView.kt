package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.app.data.model.service.form.BigTextField
import de.app.databinding.ApplicationFormBigTextBinding
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FieldValue
import de.app.ui.util.afterTextChanged

internal class BigTextFieldView(
    private val binding: ApplicationFormBigTextBinding
): InputFieldView {

    private lateinit var id: String

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

    override fun onValueChanged(handler: () -> Unit) {
        binding.field.afterTextChanged {
            handler()
        }
    }

    override fun getView(): View {
        return binding.root
    }

    internal fun populate(field: BigTextField): FieldView {
        binding.label.text = field.label
        binding.field.hint = field.hint
        id = field.id
        return this
    }

    companion object {
        fun inflate(inflater: LayoutInflater, parent: ViewGroup) = BigTextFieldView(
            ApplicationFormBigTextBinding.inflate(inflater, parent, false))
    }
}