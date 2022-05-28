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
        is TextInfoField -> TextInfoFieldView.inflate(inflater, parent).populate(field)
        is AttachmentField -> TODO()
        is BigTextField -> BigTextFieldView.inflate(inflater, parent).populate(field)
        is TextField -> TextFieldView.inflate(inflater, parent).populate(field)
        is DateField -> DateFieldView.inflate(inflater, parent).populate(field, fragment)
        is DocumentInfoField -> TODO()
        is EmailField -> TODO()
        is ImageField -> TODO()
        is MultipleChoiceField -> TODO()
        is NumberField -> TODO()
        is SingleChoiceField -> TODO()
    }
}