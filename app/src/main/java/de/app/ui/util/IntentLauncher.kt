package de.app.ui.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.documentfile.provider.DocumentFile
import de.app.data.model.FileHeader
import java.lang.IllegalStateException
import java.util.*

class FilePickerIntentLauncher(
    activity: ComponentActivity,
    key: String = UUID.randomUUID().toString(),
    handleFailure: (Throwable) -> Unit = { activity.toast("Failed to open a file: ${it.message}") },
    handleResult: (FileHeader) -> Unit
) : IntentLauncher<String, Result<Uri>>(
    registry = activity.activityResultRegistry,
    key = key,
    createIntent = { _, input -> createFilePickerIntent(input) },
    parseResult = { _, intent -> parseUriResult(intent) },
    handleResult = { rs ->
        rs.mapCatching { uri ->
            uri to DocumentFile.fromSingleUri(activity, uri)!!
        }.mapCatching {
            val name = it.second.name ?: throw IllegalStateException("Document does not have a name ${it.first}")
            val type = it.second.type ?: throw IllegalStateException("Document does not have a type ${it.first}")
            handleResult(FileHeader(name, it.first, type))
        }.onFailure { handleFailure(it) }
    }
)

class FileSaverIntentLauncher(
    activity: ComponentActivity,
    key: String = UUID.randomUUID().toString(),
    handleFailure: (Throwable) -> Unit = { activity.toast("Failed to save a file: ${it.message}") },
    handleResult: (FileHeader) -> Unit
) : IntentLauncher<FileHeader, Result<Uri>>(
    registry = activity.activityResultRegistry,
    key = key,
    createIntent = { _, input -> createFileSaverIntent(input) },
    parseResult = { _, intent -> parseUriResult(intent) },
    handleResult = { rs ->
        rs.mapCatching { uri ->
            uri to DocumentFile.fromSingleUri(activity, uri)!!
        }.mapCatching {
            val name = it.second.name ?: throw IllegalStateException("Document does not have a name ${it.first}")
            val type = it.second.type ?: throw IllegalStateException("Document does not have a type ${it.first}")
            handleResult(FileHeader(name, it.first, type))
        }.onFailure { handleFailure(it) }
    }
)


open class IntentLauncher<I, O>(
    private val registry: ActivityResultRegistry,
    createIntent: (Context, I) -> Intent,
    parseResult: (Int, Intent?) -> O,
    private val key: String,
    private val handleResult: (O) -> Unit
) {
    private lateinit var launcher: ActivityResultLauncher<I>

    private val contract: ResultContract<I, O> = ResultContract(createIntent, parseResult)
    private val observer = LifecycleObserver {
        this.launcher = registry.register(key, contract, handleResult)
    }

    fun getObserver() = observer


    fun launch(arg: I) {
        launcher.launch(arg)
    }
}

class ResultContract<I, O>(
    private val createIntentHandler: (Context, I) -> Intent,
    private val parseResultHandler: (Int, Intent?) -> O,
) : ActivityResultContract<I, O>() {

    override fun createIntent(context: Context, input: I): Intent {
        return createIntentHandler(context, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): O =
        parseResultHandler(resultCode, intent)
}