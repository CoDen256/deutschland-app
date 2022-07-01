package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.app.api.service.form.AttachmentField
import de.app.data.model.FileHeader
import de.app.databinding.ApplicationFormAttachmentBinding
import de.app.ui.components.OpenableFileViewAdapter
import de.app.ui.safe.DataSafePickerShower
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FieldValue
import de.app.ui.util.*


class AttachmentFieldView(
    private val binding: ApplicationFormAttachmentBinding,
    private val id: String,
    private val files: List<FileHeader>
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
        return FieldValue(id, files)
    }


    override fun setOnValueChangedListener(handler: () -> Unit) {
    }

    class Inflater {
        private lateinit var binding: ApplicationFormAttachmentBinding
        private lateinit var id: String
        private val files = ArrayList<FileHeader>()
        private lateinit var adapter: OpenableFileViewAdapter


        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormAttachmentBinding.inflate(inflater, parent, true)
        }

        fun populate(field: AttachmentField, fragment: Fragment, shower: DataSafePickerShower): Inflater = apply {
            val pickFileLauncher = fragment.openDocumentLauncher { addFile(it) }
            binding.uploadFrom.uploadFileLocal.setOnClickListener {
                pickFileLauncher.launch(arrayOf(field.mimeType))
            }

            binding.uploadFrom.uploadFileDataSafe.setOnClickListener {
                shower.show { addFile(it) }
            }
            adapter = OpenableFileViewAdapter({fragment.requireActivity()}, files,
                onRemoved = {removeFile(it)}
            )

            binding.files.adapter = adapter
            binding.label.text = field.label
            id = field.id
        }

        fun build() = AttachmentFieldView(binding, id, files)

        private fun addFile(file: FileHeader) {
            files.add(0, file)
            adapter.notifyItemInserted(0)
            binding.files.apply {
                post { smoothScrollToPosition(0) }
            }
        }

        private fun removeFile(it: FileHeader) {
            val index = files.indexOf(it)
            if (index == -1) return
            files.removeAt(index)
            adapter.notifyItemRemoved(index)
        }
    }


}



