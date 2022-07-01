package de.app.ui.service.validator

import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue

class TextFieldValidator: FieldValidator() {
    override fun validate(value: FieldValue): FieldState {
        val string = value.value as String
        val error = when{
            string.endsWith("John") -> "Must not end with John"
            else -> null
        }
        return FieldState(value.id, error = error)
    }
}