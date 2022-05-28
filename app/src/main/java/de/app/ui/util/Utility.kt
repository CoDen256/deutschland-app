package de.app.ui.util

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import de.app.data.Result
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
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

fun getImage(url: String): Result<Bitmap> {
    val imageUrl = URL(url)
    (imageUrl.openConnection() as? HttpURLConnection)?.run {
        return Result.Success(BitmapFactory.decodeStream(inputStream))
    }
    return Result.Error(Exception("Cannot open HttpURLConnection"))
}

fun Activity.openFile(uri: Uri, type: String){
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, type)
    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
    startActivity(intent)
}