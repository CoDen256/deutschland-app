package de.app.ui.service.view

import android.widget.EditText
import android.widget.TextView
import de.app.ui.service.data.state.FieldState
import de.app.ui.service.verificator.Verificator
import de.app.ui.util.afterTextChanged

class EditTextView(
    val view: EditText,
    verificator: Verificator
    ) : FormFieldView(verificator) {

    override fun applyState(state: FieldState) {
        if (state.error != null){
            view.error = state.error
        }else{
            view.error = null
        }
    }

    override fun getValue(): String {
        return view.text.toString()
    }

    override fun onValueChanged(handler: () -> Unit) {
        view.afterTextChanged {
            handler()
        }
    }
}