package de.app.ui.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.Lifecycle
import de.app.data.model.FileHeader
import java.lang.IllegalStateException
import java.util.*


class FilePickerIntent (
    override val activity: ComponentActivity,
    override val key: String = UUID.randomUUID().toString(),
    private val resultHandler: (FileHeader) -> Unit
): ResultContract<String, Uri>(){

    override fun createIntent(context: Context, input: String): Intent {
        return createFilePickerIntent(input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Result<Uri> {
        return parseUriResult(intent)
    }

    override fun handleResultOnSuccess(result: Uri) {
        val doc = DocumentFile.fromSingleUri(activity, result)!!
        val name = doc.name ?: throw IllegalStateException("Document does not have a name $result")
        val type = doc.type ?: throw IllegalStateException("Document does not have a type $result")
        resultHandler(FileHeader(name, result, type))
    }

    override fun handleResultOnFailure(throwable: Throwable) {
        activity.toast("Failed to open a file: ${throwable.message}")
    }
}

class FileSaverIntent(
    override val activity: ComponentActivity,
    override val key: String = UUID.randomUUID().toString(),
    private val resultHandler: (Uri) -> Unit
): ResultContract<FileHeader, Uri>(){
    override fun createIntent(context: Context, input: FileHeader): Intent {
        return createFileSaverIntent(input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Result<Uri> {
        return  parseUriResult(intent)
    }

    override fun handleResultOnSuccess(result: Uri) {
        resultHandler(result)
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

abstract class ResultContract<I, O>: ActivityResultContract<I, Result<O>>() {
    abstract val key: String
    abstract val activity: ComponentActivity

    abstract fun handleResultOnSuccess(result: O)
    abstract fun handleResultOnFailure(throwable: Throwable)

    fun register(): ActivityResultLauncher<I>{
        return activity.activityResultRegistry.register(key, this) { result ->
            result.mapCatching {
                handleResultOnSuccess(it)
            }.onFailure {
                handleResultOnFailure(it)
            }
        }
    }

}

fun <I, O> Lifecycle.createResultLauncher(resultContract: ResultContract<I, O>): IntentLauncher<I, O> {
    return IntentLauncher(resultContract, this)
}