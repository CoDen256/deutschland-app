package de.app.ui.service.verificator

import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue

abstract class FieldValidator {
    abstract fun validate(value: FieldValue): FieldState
}