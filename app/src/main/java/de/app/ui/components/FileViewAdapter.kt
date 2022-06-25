package de.app.ui.components

import android.content.Context
import de.app.data.model.FileHeader
import de.app.databinding.CommonFileItemBinding
import de.app.ui.util.openFile

class FileViewAdapter(
    context: Context,
    fileHeaders: List<FileHeader>,
    onClickListener: (FileHeader) -> Unit = {context.openFile(it.fileUri, it.mimeType)}
)  : ListViewAdapter<FileHeader, CommonFileItemBinding>(
    {inflater, parent -> CommonFileItemBinding.inflate(inflater, parent, false) },
    fileHeaders,
    {file, binding ->
        binding.file.setOnClickListener { onClickListener(file) }
        binding.fileName.text = file.name
    }
)