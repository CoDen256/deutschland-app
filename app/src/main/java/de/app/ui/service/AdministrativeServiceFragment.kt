package de.app.ui.service

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.app.data.model.service.form.*
import de.app.databinding.FragmentAdministrativeServiceBinding
import de.app.ui.service.picker.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class AdministrativeServiceFragment : Fragment() {

    private lateinit var viewModel: AdminServiceViewModel
    private lateinit var binding: FragmentAdministrativeServiceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdministrativeServiceBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AdminServiceViewModel::class.java]

        val formInflater = FormFieldInflater(inflater, binding.layout)
        viewModel.applicationForm.form.forEach {
            binding.layout.addView(convertFormFieldToView(it, formInflater))
        }


        binding.layout.addView(formInflater.inflateButton().root)
        return binding.root
    }

    private fun convertFormFieldToView(
        actual: FormField,
        inflater: FormFieldInflater
    ) = when (actual) {
        is InfoField -> inflater.inflateTextView().apply {
            root.text = actual.text
        }
        is DocumentField -> TODO()
        is ImageField -> TODO()
        is TextField -> inflater.inflateEditText().apply {
            label.text = actual.label
            field.hint = actual.hint
        }
        is BigTextField -> inflater.inflateEditTextBig().apply {
            label.text = actual.label
            field.hint = actual.hint
        }
        is EmailField -> TODO()
        is NumberField -> TODO()
        is SingleChoiceField -> TODO()
        is MultipleChoiceField -> TODO()
        is DateField -> inflater.inflateDate().apply {
            label.text = actual.label
//            field.hint = actual.hint
            dateField.setOnFocusChangeListener { _, isFocused ->
                if(isFocused) DatePickerFragment { view, selectedyear, selectedmonth, selectedday ->
                    val myCalendar: Calendar = Calendar.getInstance()
                    myCalendar.set(Calendar.YEAR, selectedyear)
                    myCalendar.set(Calendar.MONTH, selectedmonth)
                    myCalendar.set(Calendar.DAY_OF_MONTH, selectedday)
                    val myFormat = "dd/MM/yy" //Change as you need

                    val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)
                    dateField.setText(sdf.format(myCalendar.time))

                }.show(parentFragmentManager, "timePicker")
            }
        }
        is AttachmentField -> TODO()
    }.root

}