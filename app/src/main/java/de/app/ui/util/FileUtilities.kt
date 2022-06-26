package de.app.ui.util

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.createChooser
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import androidx.core.graphics.createBitmap
import androidx.core.net.toUri
import de.app.core.successOrElse
import de.app.data.model.FileHeader
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


fun Context.openFile(uri: Uri, type: String) {
    val intent = Intent(ACTION_VIEW)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.setDataAndType(uri, type)
    startActivity(intent)
}

fun Context.openUrl(uri: Uri){
    val intent = Intent(ACTION_VIEW)
    intent.data = uri
    startActivity(intent)
}


fun Context.writeTo(from: FileHeader, to: Uri) {
    val target = contentResolver.openOutputStream(to) ?: return
    val source = when {
        from.uri.scheme?.startsWith("http") == true -> URL(from.uri.toString()).openStream()
        else -> contentResolver.openInputStream(from.uri)
    } ?: return

    target.use { source.copyTo(it) }
}

fun Context.loadFirstPage(file: FileHeader): Result<Bitmap> {
    return try {
        getFirstPage(file).successOrElse(
            IllegalStateException("Unable to open file: ${file.name} ${file.uri}, ${file.mimeType}"))
    }catch (ex: Exception){
        Result.failure(ex)
    }
}

private fun Context.getFirstPage(file: FileHeader): Bitmap? {
    var toRemove: File? = null
    try {
        var uri: Uri = file.uri
        if (file.uri.scheme?.startsWith("http") == true) {
            val temp = File.createTempFile(file.name, ".pdf", cacheDir)
            uri = temp.toUri()
            toRemove = temp
            writeTo(file, uri)
        }

        return contentResolver.openFileDescriptor(uri, "r")?.let {
            val pdfRenderer = PdfRenderer(it)
            val currentPage = pdfRenderer.openPage(0)

            val bitmap = createBitmap(100, 100, Bitmap.Config.ARGB_8888)

            currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            bitmap
        }
    } finally {
        toRemove?.deleteOnExit()
    }
}


fun parseUriResult(result: Intent?): Result<Uri> {
    val intent = result
        ?: return Result.failure(IllegalStateException("No result"))
    val data = intent.data
        ?: return Result.failure(IllegalStateException("No file URI"))
    return Result.success(data)
}

fun createFilePickerIntent(mimeType: String): Intent {
    val chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = mimeType
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    return createChooser(chooseFile, "Choose a file")
}

fun createFileSaverIntent(header: FileHeader):Intent {
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = header.mimeType
        putExtra(Intent.EXTRA_TITLE, header.name)
    }
    return createChooser(intent, "Save locally")
}