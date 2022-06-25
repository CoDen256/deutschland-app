package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.app.api.service.form.DocumentInfoField
import de.app.databinding.ApplicationFormDocumentInfoBinding
import de.app.ui.components.FileViewAdapter
import de.app.ui.components.OpenableFileViewAdapter


class DocumentInfoFieldView (private val binding: ApplicationFormDocumentInfoBinding
): FieldView {

    class Inflater {
        private lateinit var binding: ApplicationFormDocumentInfoBinding

        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormDocumentInfoBinding.inflate(inflater, parent, true)
        }

        fun populate(field: DocumentInfoField, fragment: Fragment): Inflater = apply {
            binding.label.text = field.label

            binding.files.adapter = OpenableFileViewAdapter(
                fragment.requireContext(),
                field.documents
            )
        }

        fun build() = DocumentInfoFieldView(binding)
    }
}