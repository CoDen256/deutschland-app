package de.app.ui.service.view

import android.view.View
import de.app.databinding.ApplicationFormTextBinding
import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue
import de.app.ui.util.afterTextChanged

class TextFieldView(
    override val id: String,
    private val binding: ApplicationFormTextBinding,
    ) : InputFieldView {

    private val field = binding.field
    override val view: View
        get() = binding.root

    override fun applyState(state: FieldState) {
        if (state.error != null){
            field.error = state.error
        }else{
            field.error = null
        }
    }

    override fun getCurrentValue(): FieldValue {
        return FieldValue(id, field.text.toString())
    }

    override fun onValueChanged(handler: () -> Unit) {
        field.afterTextChanged {
            handler()
        }
    }
}