package de.app.ui.service.view

import android.widget.EditText
import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue
import de.app.ui.util.afterTextChanged

class EditTextView(
    val id: String,
    val view: EditText,
    ) : FieldView() {

    override fun applyState(state: FieldState) {
        if (state.error != null){
            view.error = state.error
        }else{
            view.error = null
        }
    }

    override fun getValue(): FieldValue {
        return FieldValue(id, view.text.toString())
    }

    override fun onValueChanged(handler: () -> Unit) {
        view.afterTextChanged {
            handler()
        }
    }
}