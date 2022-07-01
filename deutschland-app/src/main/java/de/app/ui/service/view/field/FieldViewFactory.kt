package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.app.api.service.form.*
import de.app.ui.safe.DataSafePickerShower


class FieldViewFactory(
    private val fragment: Fragment,
    private val inflater: LayoutInflater,
    private val parent: ViewGroup,
    private val shower: DataSafePickerShower,
) {

    fun createFieldView(field: Field): FieldView = when (field) {
        is TextInfoField -> TextInfoFieldView.Inflater()
            .inflate(inflater, parent)
            .populate(field)
            .build()
        is AttachmentField -> AttachmentFieldView.Inflater()
            .inflate(inflater, parent)
            .populate(field, fragment, shower)
            .build()
        is BigTextField -> BigTextFieldView.Inflater()
            .inflate(inflater, parent)
            .populate(field)
            .build()
        is TextField -> TextFieldView.Inflater()
            .inflate(inflater, parent)
            .populate(field)
            .build()
        is DateField -> DateFieldView.Inflater()
            .inflate(inflater, parent)
            .populate(field, fragment)
            .build()
        is DocumentInfoField -> DocumentInfoFieldView.Inflater()
            .inflate(inflater, parent)
            .populate(field, fragment)
            .build()
        is EmailField -> EmailFieldView.Inflater()
            .inflate(inflater, parent)
            .populate(field)
            .build()
        is ImageField -> ImageFieldView.Inflater()
            .inflate(inflater, parent)
            .populate(field,fragment)
            .build()
        is MultipleChoiceField -> MultipleChoiceFieldView.Inflater()
            .inflate(inflater, parent)
            .populate(field)
            .build()
        is NumberField -> NumberFieldView.Inflater()
            .inflate(inflater, parent)
            .populate(field)
            .build()
        is SingleChoiceField -> SingleChoiceFieldView.Inflater()
            .inflate(inflater, parent)
            .populate(field, fragment)
            .build()
        is RadioChoiceField -> RadioChoiceFieldView.Inflater()
            .inflate(inflater,parent)
            .populate(field)
            .build()
    }
}