package de.app.ui.service.view.field

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.app.data.model.service.form.DocumentInfoField
import de.app.databinding.ApplicationFormDocumentInfoBinding
import de.app.databinding.ApplicationFormDocumentInfoItemBinding
import de.app.ui.util.openFile


class DocumentInfoFieldView (private val binding: ApplicationFormDocumentInfoBinding
): FieldView {

    class Inflater {
        private lateinit var binding: ApplicationFormDocumentInfoBinding
        private lateinit var documentBinding : () -> ApplicationFormDocumentInfoItemBinding

        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormDocumentInfoBinding.inflate(inflater, parent, true)
            documentBinding = {
                ApplicationFormDocumentInfoItemBinding.inflate(inflater, binding.documents, true)
            }
        }

        fun populate(field: DocumentInfoField, fragment: Fragment): Inflater = apply {
            binding.label.text = field.label

            field.documents.forEach { file ->
                val documentBinding = documentBinding()
                documentBinding.label.text = file.name
                documentBinding.document.setOnClickListener {
                    val uri: Uri = Uri.parse(file.fileUri)
                    fragment.requireActivity().openFile(uri, file.mimeType)
                }
            }

        }

        fun build() = DocumentInfoFieldView(binding)
    }
}