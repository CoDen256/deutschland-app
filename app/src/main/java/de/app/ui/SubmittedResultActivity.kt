package de.app.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.databinding.ActivitySubmittedResultBinding
import de.app.ui.util.runActivity
import javax.inject.Inject

@AndroidEntryPoint
class SubmittedResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubmittedResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySubmittedResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submit.setOnClickListener {
            runActivity(MainActivity::class.java)
        }
        intent.extras?.let {
            handleBundle(it)
        }
        intent.data?.let {
            handleUriDeepLinkRedirect(intent.action, it)
        }
    }

    private fun handleBundle(data: Bundle) {
        setInfoByValueResolver { data.getString(it) }
    }


    private fun handleUriDeepLinkRedirect(intentAction: String?, uri: Uri) {
        if (Intent.ACTION_VIEW == intentAction) {
            setInfoByValueResolver { uri.getQueryParameter(it) }
        }
    }

    private fun setInfoByValueResolver(resolver: (String) -> String?) {
        binding.details.text = getString(
            R.string.form_submitted_details,
            resolver("serviceName") ?: "<empty>",
            resolver("applicationId") ?: "<empty>",
            resolver("sentDate") ?: "<empty>",
            resolver("accountDisplayName") ?: "<empty>",
            resolver("accountId") ?: "<empty>"
        )
    }
}