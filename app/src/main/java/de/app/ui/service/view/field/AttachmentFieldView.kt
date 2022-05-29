package de.app.ui.service.view.field

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import de.app.data.model.service.form.AttachmentField
import de.app.databinding.ApplicationFormAttachmentBinding
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FieldValue


class AttachmentFieldView(
    private val binding: ApplicationFormAttachmentBinding,
    private val id: String
) : InputFieldView {

    override fun applyState(formState: FormState) {
        formState.getFieldState(id)?.apply {
//            if (error != null) {
//                binding.field.error = error
//            } else {
//                binding.field.error = null
//            }
        }
    }

    override fun getValue(): FieldValue {
        return FieldValue(id, "")
    }

    override fun onValueChanged(handler: () -> Unit) {
//        binding.field.afterTextChanged {
//            handler()
//        }
    }

    override fun getView(): View {
        return binding.root
    }

    class Inflater {
        private lateinit var binding: ApplicationFormAttachmentBinding
        private lateinit var id: String

        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormAttachmentBinding.inflate(inflater, parent, true)
        }

        fun populate(field: AttachmentField, fragment: Fragment): Inflater = apply {
            binding.label.text = field.label

            binding.field.setOnClickListener {


            }

            id = field.id
        }

        fun build() = AttachmentFieldView(binding, id)
    }
}


