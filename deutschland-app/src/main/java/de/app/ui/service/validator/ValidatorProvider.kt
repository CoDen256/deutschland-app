package de.app.ui.service.validator

import de.app.api.service.form.*

class ValidatorProvider {
    fun getValidator(field: InputField): FieldValidator = when(field){
        is AttachmentField -> AttachmentFieldValidator()
        is BigTextField -> TextFieldValidator()
        is DateField -> {
            if (field.id.endsWith("birthday"))
                BirthdayDateFieldValidator(field.required)
            else
                DateFieldValidator(field.required)
        }
        is EmailField -> EmailFieldValidator()
        is MultipleChoiceField -> MultipleChoiceFieldValidator()
        is NumberField -> NumberFieldValidator()
        is SingleChoiceField -> TextFieldValidator()
        is TextField -> TextFieldValidator()
        is RadioChoiceField -> AttachmentFieldValidator()
    }
}