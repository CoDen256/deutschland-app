package de.app.ui.util

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import de.app.data.Result
import java.io.FileDescriptor
import java.net.HttpURLConnection
import java.net.URL
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


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
        return Result.Success(BitmapFactory.decodeStream(inputStream))
    }
    return Result.Error(Exception("Cannot open HttpURLConnection"))
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