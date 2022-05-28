package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import de.app.data.model.service.form.DateField
import de.app.databinding.ApplicationFormDateBinding
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FieldValue
import de.app.ui.util.afterTextChanged
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal class DateFieldView(
    private val binding: ApplicationFormDateBinding,
) : InputFieldView {

    private lateinit var id: String

    override fun applyState(formState: FormState) {
        formState.getFieldState(id)?.apply {
            if (error != null){
                binding.dateField.error = error
            }else{
                binding.dateField.error = null
            }
        }
    }

    override fun getValue(): FieldValue {
        return FieldValue(id, binding.dateField.text.toString())
    }

    override fun onValueChanged(handler: () -> Unit) {
        binding.dateField.afterTextChanged {
            handler()
        }
    }

    override fun getView(): View {
        return binding.root
    }


    internal fun populate(field: DateField, fragment: Fragment): FieldView{
        binding.label.text = field.label
        binding.dateField.apply {
            hint = field.hint
            setOnFocusChangeListener { _, isFocused ->
                if (isFocused) showPicker(fragment)
            }
            setOnClickListener { showPicker(fragment) }
        }
        id = field.id
        return this
    }

    companion object {
        fun inflate(inflater: LayoutInflater, parent: ViewGroup) = DateFieldView(
            ApplicationFormDateBinding.inflate(inflater, parent, false))
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