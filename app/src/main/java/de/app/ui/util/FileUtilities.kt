package de.app.ui.util

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.createChooser
import android.net.Uri
import de.app.data.model.FileHeader


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