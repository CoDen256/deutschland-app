package de.app.ui.service.inflater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import de.app.data.model.service.form.*
import de.app.databinding.*
import de.app.ui.service.view.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class FieldInflater(
    private val inflater: LayoutInflater,
    private val parent: ViewGroup,
) {


    fun inflateTextView(): ApplicationFormInfoBinding {
        return ApplicationFormInfoBinding
            .inflate(inflater, parent, false)
    }

    fun inflateEditText(): ApplicationFormTextBinding {
        return ApplicationFormTextBinding
            .inflate(inflater, parent, false)
    }

    fun inflateEditTextBig(): ApplicationFormBigTextBinding {
        return ApplicationFormBigTextBinding
            .inflate(inflater, parent, false)
    }


    fun inflateDate(): ApplicationFormDateBinding {
        return ApplicationFormDateBinding
            .inflate(inflater, parent, false)
    }


    fun inflateButton(): ApplicationFormSubmitBinding {
        return ApplicationFormSubmitBinding
            .inflate(inflater, parent, false)
    }


    fun convertFormFieldToView(
        actual: Field, fragment: Fragment
    ): FieldView =
        when (actual) {
            is InfoField ->InfoFieldView(inflateTextView().apply {
                root.text = actual.text
            })
            is DocumentField -> TODO()
            is ImageField -> TODO()
            is TextField -> TextFieldView(actual.id, inflateEditText().apply {
                label.text = actual.label
                field.hint = actual.hint
            })
            is BigTextField -> BigTextFieldView(actual.id,inflateEditTextBig().apply {
                label.text = actual.label
                field.hint = actual.hint
            })
            is EmailField -> TODO()
            is NumberField -> TODO()
            is SingleChoiceField -> TODO()
            is MultipleChoiceField -> TODO()
            is DateField -> DateFieldView(actual.id,inflateDate().apply {
                label.text = actual.label
                dateField.hint = actual.hint
                dateField.setOnFocusChangeListener { _, isFocused ->
                    if (isFocused) dateField.showPicker(fragment)
                }
                dateField.setOnClickListener { dateField.showPicker(fragment) }
            })
            is AttachmentField -> TODO()
        }


    private fun TextView.showPicker(fragment: Fragment) {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    text = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                        .withZone(ZoneId.of("CET"))
                        .format(Instant.ofEpochMilli(it))
                }
            }
            .show(fragment.parentFragmentManager, "datePicker")
    }

}