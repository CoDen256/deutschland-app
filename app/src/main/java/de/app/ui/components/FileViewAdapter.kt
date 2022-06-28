package de.app.ui.components

import android.app.Activity
import de.app.core.inSeparateThread
import de.app.data.model.FileHeader
import de.app.databinding.CommonFileItemBinding
import de.app.ui.util.loadFirstPage
import de.app.ui.util.openFile
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class OpenableFileViewAdapter(
    activity: () -> Activity,
    fileHeaders: List<FileHeader>,
    onLongClickListener: (FileHeader) -> Unit = {},
) : FileViewAdapter(fileHeaders, activity, onClickListener = { activity().openFile(it.uri, it.mimeType) },
    onLongClickListener = onLongClickListener
    )

open class FileViewAdapter(
    fileHeaders: List<FileHeader>,
    activity: () -> Activity,
    onLongClickListener: (FileHeader) -> Unit = {},
    onClickListener: (FileHeader) -> Unit,
) : ListViewAdapter<FileHeader, CommonFileItemBinding>(
    { inflater, parent -> CommonFileItemBinding.inflate(inflater, parent, false) },
    fileHeaders,
    { file, binding ->
        executor.submit {
            activity().apply {
                loadFirstPage(file).onSuccess {
                    runOnUiThread {
                        binding.file.setImageBitmap(it)
                    }
                }
            }
        }
        binding.file.setOnClickListener { onClickListener(file) }
        binding.file.setOnLongClickListener {
            onLongClickListener(file)
            true
        }
        binding.fileName.text = file.name
    }
) {
    companion object{
        val executor: ExecutorService = Executors.newCachedThreadPool()
    }
}