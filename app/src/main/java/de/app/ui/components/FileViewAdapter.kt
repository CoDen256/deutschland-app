package de.app.ui.components

import android.app.Activity
import android.widget.TextView
import de.app.R
import de.app.data.model.FileHeader
import de.app.databinding.CommonFileItemBinding
import de.app.ui.util.loadFirstPage
import de.app.ui.util.openFile
import java.lang.Integer.min
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class OpenableFileViewAdapter(
    activity: () -> Activity,
    fileHeaders: List<FileHeader>,
    onLongClickListener: (FileHeader) -> Unit = {},
) : FileViewAdapter(fileHeaders, activity, onClickListener = { activity().openFile(it.uri, it.mimeType) },
    onLongClickListener = {  }
    )

open class FileViewAdapter(
    fileHeaders: List<FileHeader>,
    activity: () -> Activity,
    onLongClickListener: (FileHeader) -> Unit = {},
    onCreateContextMenuListener: (FileHeader) -> Unit = {},
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
        binding.root.setOnClickListener { onClickListener(file) }
        binding.root.setOnCreateContextMenuListener { menu, v, menuInfo ->
            val textView = TextView(activity())
            menu.add(textView)
            menu.add(0, 0,0, activity().getString(R.string.file_context_remove))
            menu.add(0, 1,1, activity().getString(R.string.file_context_download))
        }
        binding.root.setOnLongClickListener { binding.file.showContextMenu(0f, 0f) }
        binding.root.setOnContextClickListener {
            it.
        }
        binding.fileName.text = cutName(file.name)
    }
) {
    companion object{
        val executor: ExecutorService = Executors.newCachedThreadPool()
    }
}

private fun cutName(name: String): String {
    val ext = name.substringAfterLast(".")
    val filename = name.substringBeforeLast(".")
    return filename.substring(0, min(5, filename.length)) + "..." + ext
}