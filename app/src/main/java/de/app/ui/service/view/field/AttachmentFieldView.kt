package de.app.ui.service.view.field

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import de.app.api.service.form.AttachmentField
import de.app.databinding.ApplicationFormAttachmentBinding
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FieldValue
import de.app.ui.util.*


class AttachmentFieldView(
    private val binding: ApplicationFormAttachmentBinding,
    private val id: String,
    private val uriHolder: MutableLiveData<Uri>
) : InputFieldView {

    override fun applyState(formState: FormState) {
        formState.getFieldState(id)?.apply {
            if (error != null) {
                binding.label.error = error
            } else {
                binding.label.error = null
            }
        }
    }

    override fun getValue(): FieldValue {
        return FieldValue(id, uriHolder.value)
    }


    override fun setOnValueChangedListener(handler: () -> Unit) {
        binding.filePath.afterTextChanged { handler() }
    }

    class Inflater {
        private lateinit var binding: ApplicationFormAttachmentBinding
        private lateinit var id: String
        private val uriHolder: MutableLiveData<Uri> = MutableLiveData()


        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormAttachmentBinding.inflate(inflater, parent, true)
        }

        fun populate(field: AttachmentField, fragment: Fragment): Inflater = apply {

            val launcher = fragment.openDocumentLauncher{
                binding.filePath.text = it.name
                uriHolder.value = it.uri
            }

            binding.label.text = field.label
            binding.field.setOnClickListener {
                launcher.launch(arrayOf(field.mimeType))
            }

            id = field.id
        }

        fun build() = AttachmentFieldView(binding, id, uriHolder)
    }
}



