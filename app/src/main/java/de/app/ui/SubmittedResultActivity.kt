package de.app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.app.R
import de.app.databinding.ActivitySubmittedResultBinding
import de.app.ui.util.runActivity

class SubmittedResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubmittedResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySubmittedResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submit.setOnClickListener {
            runActivity(MainActivity::class.java)
        }
        handleIntent(intent)
    }


    private fun handleIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            appLinkData?.let {
                setInfo(
                    serviceName = it.getQueryParameter("serviceName"),
                    accountDisplayName = it.getQueryParameter("accountId"),
                    applicationId = it.getQueryParameter("applicationId"),
                    accountId = it.getQueryParameter("accountDisplayName"),
                    sentDate = it.getQueryParameter("sentDate")
                )
            }
        }
    }

    private fun setInfo(
        serviceName: String?, applicationId: String?,
        accountDisplayName: String?, accountId: String?,
        sentDate: String?
    ) {
        binding.details.text = getString(
            R.string.form_submitted_details,
            serviceName ?: "<empty>",
            applicationId ?: "<empty>",
            sentDate ?: "<empty>",
            accountDisplayName ?: "<empty>",
            accountId ?: "<empty>"
        )
    }
}