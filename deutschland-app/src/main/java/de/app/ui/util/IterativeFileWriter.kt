package de.app.ui.util

import android.net.Uri
import androidx.activity.ComponentActivity
import de.app.core.inSeparateThread
import de.app.data.model.FileHeader

class IterativeFileWriter(
    private val activity: ComponentActivity,
    private val launcher: (FileHeader) -> Unit
) {
    private val downloadingQueue: MutableList<FileHeader> = ArrayList()

    fun saveNextTo(uri: Uri){
        inSeparateThread {
            activity.apply {
                writeTo(downloadingQueue.removeFirst(), uri)
                runOnUiThread { toast("Successfully written ${uri.lastPathSegment}") }
                triggerDownloadNext()
            }
        }
    }

    private fun triggerDownloadNext() {
        if (downloadingQueue.isEmpty()) return
        launcher(downloadingQueue.first())
    }

    fun push(files: List<FileHeader>){
        downloadingQueue.clear()
        downloadingQueue.addAll(files)
        triggerDownloadNext()
    }
}