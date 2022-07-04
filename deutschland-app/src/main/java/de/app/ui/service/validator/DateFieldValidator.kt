package de.app.ui.service.validator

import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue

class DateFieldValidator(override val required: Boolean): FieldValidator() {
    override fun validate(value: FieldValue): FieldState {
        return FieldState(value.id, error=validateRequired(value.value != null && (value.value as String).isNotBlank()))
    }
}