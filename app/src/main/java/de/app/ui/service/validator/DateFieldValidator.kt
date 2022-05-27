package de.app.ui.service.validator

import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue

class DateFieldValidator: FieldValidator() {
    override fun validate(value: FieldValue): FieldState {
        val string = value.value as String
        val error = when{
            string.endsWith("2022") -> "Must not end with 2022"
            else -> null
        }
        return FieldState(value.id, error = error)
    }
}