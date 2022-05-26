package de.app.ui.service.view

import android.widget.TextView
import de.app.ui.service.data.state.FieldState

class TextFieldView(val name: String,val view: TextView) : FormFieldView() {
    override fun applyState(state: FieldState) {
        if (state.name == name && state.error != null){
            view.error = state.error
        }
    }

    override fun getValue(): String {
        return view.text.toString()
    }
}