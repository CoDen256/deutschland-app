package de.app.ui.util

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri


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