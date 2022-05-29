package de.app.ui.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class LifecycleObserver(private val registry: ActivityResultRegistry) : DefaultLifecycleObserver{
    lateinit var getContent: ActivityResultLauncher<String>

    override fun onCreate(owner: LifecycleOwner){
        getContent = registry.register("key", object :
            ActivityResultContract<String, Uri>() {
            override fun createIntent(context: Context, input: String?): Intent {
                val chooseFile = Intent(Intent.ACTION_GET_CONTENT)
                chooseFile.type = input!!
                return Intent.createChooser(chooseFile, "Choose a file")
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Uri {
                return intent!!.data!!
            }

        }){ result: Uri ->
            result
        }
    }

    fun getContent(mimeType: String){
        getContent.launch(mimeType)
    }
}