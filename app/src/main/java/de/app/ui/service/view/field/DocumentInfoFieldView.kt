package de.app.ui.service.view.field

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.app.api.service.form.DocumentInfoField
import de.app.databinding.ApplicationFormDocumentInfoBinding
import de.app.databinding.ApplicationFormDocumentInfoItemBinding
import de.app.ui.util.FileViewAdapter
import de.app.ui.util.openFile


class DocumentInfoFieldView (private val binding: ApplicationFormDocumentInfoBinding
): FieldView {

    class Inflater {
        private lateinit var binding: ApplicationFormDocumentInfoBinding

        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormDocumentInfoBinding.inflate(inflater, parent, true)
        }

        fun populate(field: DocumentInfoField, fragment: Fragment): Inflater = apply {
            binding.label.text = field.label

            binding.files.adapter = FileViewAdapter(
                fragment.requireContext(),
                field.documents
            )
        }

        fun build() = DocumentInfoFieldView(binding)
    }
}