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
        is MultipleChoiceField -> MultipleChoiceFieldValidator()
        is NumberField -> NumberFieldValidator()
        is SingleChoiceField -> TextFieldValidator()
        is TextField -> TextFieldValidator()
        is RadioChoiceField -> AttachmentFieldValidator()
    }
}