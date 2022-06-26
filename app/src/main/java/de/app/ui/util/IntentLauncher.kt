package de.app.ui.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import de.app.data.model.FileHeader
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
    parseResult = { _, intent -> parseFilePickerResult(intent) },
    handleResult = { rs ->
        rs.onSuccess { uri ->
            val contentResolver = activity.contentResolver
            contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            val file = FileHeader(
                uri.getFileName(contentResolver)!!,
                uri,
                "application/pdf"
            )
            handleResult(file)
        }.onFailure { handleFailure(it) }
    }
)

open class IntentLauncher<I, O>(
    private val registry: ActivityResultRegistry,
    createIntent: (Context, I?) -> Intent,
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