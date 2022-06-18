package de.app.ui.service

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class IntentLauncher<I, O>(
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

    fun getObserver(): DefaultLifecycleObserver{
        return observer
    }

    fun launch(arg: I){
        launcher.launch(arg)
    }
}

class ResultContract<I, O>(
    private val createIntentHandler: (Context, I?) -> Intent,
    private val parseResultHandler: (Int, Intent?) -> O,
): ActivityResultContract<I, O>() {

    override fun parseResult(resultCode: Int, intent: Intent?): O =
        parseResultHandler(resultCode, intent)

    override fun createIntent(context: Context, input: I): Intent {
        return createIntentHandler(context, input)
    }
}

class LifecycleObserver(
    private val onCreateObserver: (LifecycleOwner) -> Unit
): DefaultLifecycleObserver{
    override fun onCreate(owner: LifecycleOwner){
        onCreateObserver(owner)
    }
}