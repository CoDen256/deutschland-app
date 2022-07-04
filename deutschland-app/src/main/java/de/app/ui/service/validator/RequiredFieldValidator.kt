package de.app.ui.service.validator

import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue

class RequiredFieldValidator(private val required: Boolean): FieldValidator() {
    override fun validate(value: FieldValue): FieldState {
        if (!required) return FieldState(value.id, error = null)
        if (value.value == null){
            return FieldState(value.id, error = "Feld is erfordelich")
        }
        if (value.value is String){
            if (value.value.isBlank()){
                return FieldState(value.id, error = "Feld is erfordelich")
            }
        }
        return FieldState(value.id, error = null)
    }
}