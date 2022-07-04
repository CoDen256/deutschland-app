package de.app.ui.service.validator

import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue

abstract class FieldValidator {
    abstract fun validate(value: FieldValue): FieldState

    open val required: Boolean = false
    fun validateRequired(present: Boolean): String?{
        if (required && !present){
            return "Field required"
        }
        return null
    }
}