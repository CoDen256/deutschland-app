package de.app.ui.service.validator

import de.app.api.service.form.*

class ValidatorProvider {
    fun getValidator(field: InputField): FieldValidator = RequiredFieldValidator(field.required)
}