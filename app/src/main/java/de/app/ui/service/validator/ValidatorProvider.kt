package de.app.ui.service.validator

import de.app.api.service.form.*

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