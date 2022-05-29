package de.app.ui.service.validator

import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue

class AttachmentFieldValidator:FieldValidator() {
    override fun validate(value: FieldValue): FieldState {
        return FieldState(value.id, error = null)
    }
}