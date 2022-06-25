package de.app.ui.util

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.LifecycleOwner
import de.app.core.success
import de.app.data.model.FileHeader
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

fun Context.openFile(uri: Uri, type: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, type)
    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
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

fun extractFileHeader(bundle: Bundle): Result<FileHeader> {
    val filename = bundle.getString("filename")
        ?: return Result.failure(IllegalArgumentException("No filename provided"))
    val uri = bundle.getParcelable<Uri>("fileUri")
        ?: return Result.failure(IllegalArgumentException("No fileUri provided"))
    val mimeType = bundle.getString("mimeType")
        ?: return Result.failure(IllegalArgumentException("No mimeType provided"))
    return FileHeader(filename, uri, mimeType).success()
}

fun bundleFromFileHeader(fileHeader: FileHeader): Bundle {
    return bundleOf(
        "filename" to fileHeader.name,
        "fileUri" to fileHeader.fileUri,
        "mimeType" to fileHeader.mimeType
    )
}

fun FragmentActivity.setFileResultListener(
    key: String,
    lifecycleOwner: LifecycleOwner,
    onFailureListener: (Throwable) -> Unit = { this.toast("Failed to get result for $key: ${it.message}") },
    onSuccessListener: (FileHeader) -> Unit
) {
    this.supportFragmentManager.setFragmentResultListener(key,lifecycleOwner) { k, bundle ->
        extractFileHeader(bundle).onSuccess {
            onSuccessListener(it)
        }.onFailure {
            onFailureListener(it)
        }
    }
}