package de.app.ui.components

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import androidx.core.graphics.createBitmap
import de.app.core.inSeparateThread
import de.app.data.model.FileHeader
import de.app.databinding.CommonFileItemBinding
import de.app.ui.util.openFile
import java.io.FileNotFoundException
import java.lang.IllegalStateException
import java.util.concurrent.Executors

class OpenableFileViewAdapter(
    activity: Activity,
    fileHeaders: List<FileHeader>,
) : FileViewAdapter(fileHeaders, activity, onClickListener = { activity.openFile(it.fileUri, it.mimeType) })

open class FileViewAdapter(
    fileHeaders: List<FileHeader>,
    activity: Activity,
    onClickListener: (FileHeader) -> Unit,
) : ListViewAdapter<FileHeader, CommonFileItemBinding>(
    { inflater, parent -> CommonFileItemBinding.inflate(inflater, parent, false) },
    fileHeaders,
    { file, binding ->
        inSeparateThread {
            firstPageBitmap(activity, file).onSuccess {
                activity.runOnUiThread {
                    binding.file.setImageBitmap(it)
                }
            }
        }
        binding.file.setOnClickListener { onClickListener(file) }
        binding.fileName.text = file.name
    }
)

private fun firstPageBitmap(context: Context, file: FileHeader): Result<Bitmap> {
    return try {
        context.contentResolver.openFileDescriptor(file.fileUri, "r")?.let {
            val pdfRenderer = PdfRenderer(it)
            val currentPage = pdfRenderer.openPage(0)

            val bitmap = createBitmap(100,100, Bitmap.Config.ARGB_8888)

            currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            Result.success(bitmap)

        } ?: Result.failure(IllegalStateException("Unable to open file: ${file.name} ${file.fileUri}, ${file.mimeType}"))
    }catch (ex: Exception){
        Result.failure(ex)
    }
}