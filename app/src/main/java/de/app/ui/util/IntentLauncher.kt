package de.app.ui.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import de.app.core.inSeparateThread
import de.app.core.successOrElse
import de.app.data.model.FileHeader
import java.lang.IllegalStateException
import java.util.*


class FilePickerIntent(
    override val activity: ComponentActivity,
    override val key: String = UUID.randomUUID().toString(),
    private val resultHandler: (FileHeader) -> Unit
) : ResultContract<String, Uri>() {

    override fun createIntentFromInput(context: Context, input: String): Intent {
        return createFilePickerIntent(input)
    }

    override fun parseResult(intent: Intent?): Result<Uri> {
        return parseUriResult(intent)
    }

    override fun handleResultOnSuccess(input: String, result: Uri) {
        val doc = DocumentFile.fromSingleUri(activity, result)!!
        val name = doc.name ?: throw IllegalStateException("Document does not have a name $result")
        val type = doc.type ?: throw IllegalStateException("Document does not have a type $result")
        resultHandler(FileHeader(name, result, type))
    }

    override fun handleResultOnFailure(throwable: Throwable) {
    }
}

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

class FileSaverIntent(
    override val activity: ComponentActivity,
    override val key: String = UUID.randomUUID().toString(),
) : ResultContract<FileHeader, Uri>() {

    override fun createIntentFromInput(context: Context, input: FileHeader): Intent {
        return createFileSaverIntent(input)
    }

    override fun parseResult(intent: Intent?): Result<Uri> {
        return parseUriResult(intent)
    }

    override fun handleResultOnSuccess(input: FileHeader, result: Uri) {
        inSeparateThread {
            activity.writeTo(input, result)
            activity.runOnUiThread {
                activity.toast("Successfully written ${result.lastPathSegment}")
            }
        }
    }

    override fun handleResultOnFailure(throwable: Throwable) {
        activity.toast("Failed to save a file: ${throwable.message}")
    }
}

open class IntentLauncher<I, O>(contract: ResultContract<I, O>, lifecycle: Lifecycle) {
    private lateinit var launcher: ActivityResultLauncher<I>

    init {
        lifecycle.addObserver(LifecycleObserver {
            launcher = contract.register()
        })
    }

    fun launch(arg: I) {
        launcher.launch(arg)
    }
}

abstract class ResultContract<I, O> : ActivityResultContract<I, Result<O>>() {
    abstract val key: String
    abstract val activity: ComponentActivity

    private var lastInput: I? = null

    override fun createIntent(context: Context, input: I): Intent {
        lastInput = input // god forgive me
        return createIntentFromInput(context, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Result<O> {
        return parseResult(intent)
    }

    abstract fun createIntentFromInput(context: Context, input: I): Intent
    abstract fun parseResult(intent: Intent?): Result<O>
    abstract fun handleResultOnSuccess(input: I, result: O)
    abstract fun handleResultOnFailure(throwable: Throwable)

    fun register(): ActivityResultLauncher<I> {
        return activity.activityResultRegistry.register(key, this) { result ->
            result.mapCatching {
                handleResultOnSuccess(lastInput!!, it)
            }.onFailure {
                handleResultOnFailure(it)
            }
        }
    }

}

fun <I, O> Lifecycle.launcher(resultContract: ResultContract<I, O>): IntentLauncher<I, O> {
    return IntentLauncher(resultContract, this)
}

fun Fragment.openDocumentLauncher(
    activity: ComponentActivity,
    resultHandler: (FileHeader) -> Unit
): ActivityResultLauncher<Array<String>> {
    return registerForActivityResult(ActivityResultContracts.OpenDocument(),
        OpenDocumentCallback(activity, resultHandler))
}