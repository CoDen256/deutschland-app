package de.app.ui.service.validator

import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue

abstract class FieldValidator {
    abstract fun validate(value: FieldValue): FieldState
}