package de.app.data.model

import android.net.Uri

data class FileHeader(
    val name: String,
    val uri: Uri,
    val mimeType: String
){
    constructor(name: String, uri: String, mimeType: String) : this(name, Uri.parse(uri), mimeType)
}