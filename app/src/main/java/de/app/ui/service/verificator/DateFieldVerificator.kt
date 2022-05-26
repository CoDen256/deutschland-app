package de.app.ui.service.verificator

import de.app.ui.service.data.state.FieldState

class DateFieldVerificator: Verificator() {
    override fun verify(name: String, value: Any): FieldState {
        val string = value as String
        val error = when{
            value.endsWith("2022") -> "Must not end with 2022"
            else -> null
        }
        return FieldState(name, error = error)
    }
}