package de.app.ui.service.validator

import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue

class NumberFieldValidator: FieldValidator() {
    override fun validate(value: FieldValue): FieldState {
        val error = when (value.value as Int?) {
            !in 10..90 -> "Must be in 10..90"
            else -> null
        }
        return FieldState(value.id, error = error)
    }
}