package de.app.ui.components

import android.app.Activity
import android.view.ActionMode
import android.view.ActionMode.TYPE_FLOATING
import android.view.Menu
import android.view.MenuInflater
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
    onRemoved: (FileHeader) -> Unit = {},
    onDownloaded: (FileHeader) -> Unit = {},
) : FileViewAdapter(fileHeaders, activity,
    onClickListener = { activity().openFile(it.uri, it.mimeType) },
    extra = { file, binding ->
        val menu = FileContextMenu(
            onDownloadClicked= {onDownloaded(file)},
            onRemoveClicked = {onRemoved(file)},
            view = binding.root,
            context = activity()
        )
        binding.root.setOnLongClickListener {
            menu.show()
            true
        }
    }
    )

open class FileViewAdapter(
    fileHeaders: List<FileHeader>,
    activity: () -> Activity,
    extra: (FileHeader, CommonFileItemBinding) -> Unit =  {_, _ ->},
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
        binding.fileName.text = cutName(file.name)
        extra(file,binding)
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