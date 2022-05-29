package de.app.ui.service.validator

import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue

class MultipleChoiceFieldValidator: FieldValidator() {
    override fun validate(value: FieldValue): FieldState {
        val string = value.value as List<String>
        val error = when{
            string.contains("One") && string.contains("Two") -> "Must not contain one and two"
            else -> null
        }
        return FieldState(value.id, error = error)
    }
}