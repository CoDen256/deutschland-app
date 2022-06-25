package de.app.ui.components

import android.content.Context
import de.app.data.model.FileHeader
import de.app.databinding.CommonFileItemBinding
import de.app.ui.util.openFile

class FileViewAdapter(
    context: Context,
    fileHeaders: List<FileHeader>
)  : ListViewAdapter<FileHeader, CommonFileItemBinding>(
    {inflater, parent -> CommonFileItemBinding.inflate(inflater, parent, false) },
    fileHeaders,
    {file, binding ->
        binding.file.setOnClickListener {
            context.openFile(file.fileUri, file.mimeType)
        }
        binding.fileName.text = file.name
    }
)