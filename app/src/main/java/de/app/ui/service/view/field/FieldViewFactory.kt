package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.app.data.model.service.form.*


class FieldViewFactory(
    private val fragment: Fragment,
    private val inflater: LayoutInflater,
    private val parent: ViewGroup
) {

    fun createFieldView(field: Field): FieldView = when (field) {
        is TextInfoField -> TextInfoFieldView.Inflater()
            .inflate(inflater, parent)
            .populate(field)
            .build()
        is AttachmentField -> TODO()
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
        is DocumentInfoField -> TODO()
        is EmailField -> TODO()
        is ImageField -> TODO()
        is MultipleChoiceField -> TODO()
        is NumberField -> TODO()
        is SingleChoiceField -> TODO()
    }
}