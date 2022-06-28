package de.app.ui.util

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import de.app.core.successOrElse
import de.app.data.model.FileHeader


class OpenDocumentCallback(
    private val activity: ComponentActivity,
    private val resultHandler: (FileHeader) -> Unit
) : ResultCallback<Uri>() {

    override fun onSuccess(result: Uri) {
        val doc = DocumentFile.fromSingleUri(activity, result)!!
        val name =
            doc.name ?: throw IllegalStateException("Document does not have a name $result")
        val type =
            doc.type ?: throw IllegalStateException("Document does not have a type $result")
        resultHandler(FileHeader(name, result, type))
    }

    override fun onFailure(error: Throwable) {
        activity.toast("Failed to open a document: ${error.message}")
    }
}

class CreateDocumentCallback(
    private val activity: ComponentActivity,
    private val resultHandler: (Uri) -> Unit
) : ResultCallback<Uri>() {

    override fun onSuccess(result: Uri) {
        resultHandler(result)
    }

    override fun onFailure(error: Throwable) {
        activity.toast("Failed to open a document: ${error.message}")
    }
}

abstract class ResultCallback<I : Any> : ActivityResultCallback<I?> {
    override fun onActivityResult(result: I?) {
        result.successOrElse(IllegalStateException("No Result for ${this.javaClass.simpleName}"))
            .mapCatching {
                onSuccess(it)
            }.onFailure {
                onFailure(it)
            }
    }

    abstract fun onSuccess(result: I)
    abstract fun onFailure(error: Throwable)
}


fun Fragment.openDocumentLauncher(
    resultHandler: (FileHeader) -> Unit
): ActivityResultLauncher<Array<String>> {
    return registerForActivityResult(ActivityResultContracts.OpenDocument(),
        OpenDocumentCallback(requireActivity(), resultHandler))
}

fun Fragment.createDocumentLauncher(
    mimeType: String,
    resultHandler: (Uri) -> Unit,
): ActivityResultLauncher<String> {
    return registerForActivityResult(ActivityResultContracts.CreateDocument(mimeType),
        CreateDocumentCallback(requireActivity(), resultHandler))
}