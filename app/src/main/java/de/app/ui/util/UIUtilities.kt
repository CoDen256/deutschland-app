package de.app.ui.util

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.google.android.material.datepicker.MaterialDatePicker
import java.net.HttpURLConnection
import java.net.URL
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun View.onClickNavigate(controller: NavController,
                         @IdRes resId: Int,
                         vararg args: Pair<String, Any?>,
                         navOptions: NavOptions? = null){
    setOnClickListener {
        controller.navigate(resId, bundleOf(*args),  navOptions)
    }
}

fun String.editable(): Editable{
    return SpannableStringBuilder(this)
}

fun TextView.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun TextView.showPicker(fragmentManager: FragmentManager) {
    MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select date")
        .build()
        .apply {
            addOnPositiveButtonClickListener {
                text = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    .withZone(ZoneId.of("CET"))
                    .format(Instant.ofEpochMilli(it))
            }
        }
        .show(fragmentManager, "datePicker")
}

fun loadImageFromUrl(url: String): Result<Bitmap> {
    val imageUrl = URL(url)
    (imageUrl.openConnection() as? HttpURLConnection)?.run {
        return Result.success(BitmapFactory.decodeStream(inputStream))
    }
    return Result.failure(Exception("Cannot open HttpURLConnection"))
}

fun Uri.getFileName(contentResolver: ContentResolver): String?{
    val query = contentResolver.query(
        this,
        null,
        null,
        null,
        null,
        null
    )
    query?.use { cursor ->
        if (cursor.moveToFirst()){
            val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (columnIndex >= 0){
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

fun Context.openFile(uri: Uri, type: String){
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