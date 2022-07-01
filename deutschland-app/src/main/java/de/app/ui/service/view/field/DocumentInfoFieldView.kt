package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import de.app.api.service.form.DocumentInfoField
import de.app.databinding.ApplicationFormDocumentInfoBinding
import de.app.ui.components.OpenableFileViewAdapter
import de.app.ui.util.IterativeFileWriter
import de.app.ui.util.createDocumentLauncher


class DocumentInfoFieldView (private val binding: ApplicationFormDocumentInfoBinding
): FieldView {

    class Inflater {
        private lateinit var binding: ApplicationFormDocumentInfoBinding
    private lateinit var createFileLauncher: ActivityResultLauncher<String>
    private lateinit var adapter: OpenableFileViewAdapter
        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormDocumentInfoBinding.inflate(inflater, parent, true)
        }

        fun populate(field: DocumentInfoField, fragment: Fragment): Inflater = apply {
            binding.label.text = field.label
            val writer = IterativeFileWriter(fragment.requireActivity()) { createFileLauncher.launch(it.name) }
            createFileLauncher = fragment.createDocumentLauncher( "application/pdf") {
                writer.saveNextTo(it)
            }
            adapter = OpenableFileViewAdapter({ fragment.requireActivity() }, field.documents,
                    onDownloaded = { writer.push(listOf(it)) }
            )

            binding.files.adapter = adapter
        }

        fun build() = DocumentInfoFieldView(binding)
    }
}