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
    activity: Activity,
    fileHeaders: List<FileHeader>,
) : FileViewAdapter(fileHeaders, activity, onClickListener = { activity.openFile(it.uri, it.mimeType) })

open class FileViewAdapter(
    fileHeaders: List<FileHeader>,
    activity: Activity,
    onClickListener: (FileHeader) -> Unit,
) : ListViewAdapter<FileHeader, CommonFileItemBinding>(
    { inflater, parent -> CommonFileItemBinding.inflate(inflater, parent, false) },
    fileHeaders,
    { file, binding ->
        executor.submit {
            activity.loadFirstPage(file).onSuccess {
                activity.runOnUiThread {
                    binding.file.setImageBitmap(it)
                }
            }
        }
        binding.file.setOnClickListener { onClickListener(file) }
        binding.fileName.text = file.name
    }
) {
    companion object{
        val executor: ExecutorService = Executors.newFixedThreadPool(4)
    }
}