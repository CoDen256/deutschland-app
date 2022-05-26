package de.app.ui.service.verificator

import de.app.ui.service.data.state.FieldState

class TextFieldVerificator: Verificator() {
    override fun verify(name: String, value: Any): FieldState {
        val string = value as String
        val error = when{
            value.endsWith("John") -> "Must not end with John"
            else -> null
        }
        return FieldState(name, error = error)
    }
}