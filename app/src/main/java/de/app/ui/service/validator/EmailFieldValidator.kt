package de.app.ui.service.validator

import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue

class EmailFieldValidator: FieldValidator() {
    private var edited: Boolean = false

    override fun validate(value: FieldValue): FieldState {
        val string = value.value as String
        if (string.isNotEmpty()) {
            edited = true
        }
        if (!edited) return FieldState(value.id, error = null)
        val error = when{
            !string.contains("@") -> "Must contain @"
            else -> null
        }
        return FieldState(value.id, error = error)
    }
}