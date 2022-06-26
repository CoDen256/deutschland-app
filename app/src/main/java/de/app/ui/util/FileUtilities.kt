package de.app.ui.util

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.net.HttpURLConnection
import java.net.URL


fun loadImageFromUrl(url: String): Result<Bitmap> {
    val imageUrl = URL(url)
    (imageUrl.openConnection() as? HttpURLConnection)?.run {
        return Result.success(BitmapFactory.decodeStream(inputStream))
    }
    return Result.failure(Exception("Cannot open HttpURLConnection"))
}

fun Uri.getFileName(contentResolver: ContentResolver): String? {
    val query = contentResolver.query(
        this,
        null,
        null,
        null,
        null,
        null
    )
    query?.use { cursor ->
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (columnIndex >= 0) {
                return cursor.getString(columnIndex)
            }
        }
    }
    return this.path
}

fun Uri.getFile(contentResolver: ContentResolver): ByteArray? {
    contentResolver.openInputStream(this)?.use { inputStream ->
        inputStream.use { stream ->
            return stream.readBytes()
        }
    }
    return null
}

fun Context.openFile(uri: Uri, type: String): Result<Unit> {
    val root = Environment.getExternalStorageDirectory().toString()
    val dir = File("$root/de-app")
    if (!dir.exists()){
        dir.mkdirs()
    }
    val builder = VmPolicy.Builder()
    StrictMode.setVmPolicy(builder.build())
    val doc: DocumentFile = DocumentFile.fromSingleUri(this, uri) ?: return Result.failure(java.lang.IllegalStateException("Unable to create Documentfiel from given uri"))

    val temp = File(dir, "1"+doc.name)
    temp.outputStream().use {
        contentResolver.openInputStream(uri)?.copyTo(it)
    }

    val intent = Intent(ACTION_VIEW)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.setDataAndType(doc.uri, doc.type)
    startActivity(intent)
    return Result.success(Unit)
}

fun Context.openUrl(uri: Uri){
    val intent = Intent(ACTION_VIEW)
    intent.data = uri
    startActivity(intent)
}


fun parseFilePickerResult(result: Intent?): Result<Uri> {
    val intent = result
        ?: return Result.failure(IllegalStateException("File picker did not return any data"))
    val data = intent.data
        ?: return Result.failure(IllegalStateException("File picker returned empty data"))
    return Result.success(data)
}

fun createFilePickerIntent(input: String?): Intent {
    val chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.addCategory(Intent.CATEGORY_OPENABLE)
    chooseFile.type = input!!
    chooseFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    return Intent.createChooser(chooseFile, "Choose a file")
}