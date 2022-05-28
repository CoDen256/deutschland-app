package de.app.ui.service.validator

import de.app.data.model.service.form.*
import de.app.ui.service.validator.DateFieldValidator
import de.app.ui.service.validator.FieldValidator
import de.app.ui.service.validator.TextFieldValidator

class ValidatorProvider {
    fun getValidator(field: InputField): FieldValidator = when(field){
        is AttachmentField -> AttachmentFieldValidator()
        is BigTextField -> TextFieldValidator()
        is DateField -> DateFieldValidator()
        is EmailField -> EmailFieldValidator()
        is MultipleChoiceField -> TODO()
        is NumberField -> TODO()
        is SingleChoiceField -> TODO()
        is TextField -> TextFieldValidator()
    }
}