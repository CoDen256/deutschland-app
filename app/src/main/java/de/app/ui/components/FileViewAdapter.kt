package de.app.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import androidx.core.graphics.createBitmap
import de.app.data.model.FileHeader
import de.app.databinding.CommonFileItemBinding
import de.app.ui.util.openFile
import java.io.FileNotFoundException
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class OpenableFileViewAdapter(
    context: Context,
    fileHeaders: List<FileHeader>,
) : FileViewAdapter(fileHeaders, context, onClickListener = { context.openFile(it.fileUri, it.mimeType) })

open class FileViewAdapter(
    fileHeaders: List<FileHeader>,
    context: Context,
    onClickListener: (FileHeader) -> Unit,
) : ListViewAdapter<FileHeader, CommonFileItemBinding>(
    { inflater, parent -> CommonFileItemBinding.inflate(inflater, parent, false) },
    fileHeaders,
    { file, binding ->
        firstPageBitmap(context, file).onSuccess {
            binding.file.setImageBitmap(it)
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
    }catch (ex: FileNotFoundException){
        Result.failure(ex)
    }
}