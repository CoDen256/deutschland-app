package de.app.ui.service.view.field

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import de.app.data.Result
import de.app.data.model.service.form.AttachmentField
import de.app.databinding.ApplicationFormAttachmentBinding
import de.app.ui.service.IntentLauncher
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FieldValue
import de.app.ui.util.afterTextChanged
import de.app.ui.util.getFileName


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
            val launcher = IntentLauncher<String, Result<Uri>>(
                fragment.requireActivity().activityResultRegistry,
                key = field.id,
                createIntent = { _, input -> createIntent(input) },
                parseResult = { _, intent -> parseResult(intent) },
                handleResult = {
                    if (it is Result.Success) {
                        binding.filePath.text = it.data.getFileName(fragment.requireActivity().contentResolver)
                        uriHolder.value = it.data
                    }
                }
            )

            fragment.lifecycle.addObserver(launcher.getObserver())

            binding.label.text = field.label
            binding.field.setOnClickListener {
                launcher.launch(field.mimeType)
            }

            id = field.id
        }

        private fun parseResult(result: Intent?): Result<Uri> {
            val intent = result
                ?: return Result.Error(IllegalStateException("File picker did not return any data"))
            val data = intent.data
                ?: return Result.Error(IllegalStateException("File picker returned empty data"))
            return Result.Success(data)
        }

        private fun createIntent(input: String?): Intent {
            val chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
            chooseFile.addCategory(Intent.CATEGORY_OPENABLE)
            chooseFile.type = input!!
            return Intent.createChooser(chooseFile, "Choose a file")
        }

        fun build() = AttachmentFieldView(binding, id, uriHolder)
    }
}



