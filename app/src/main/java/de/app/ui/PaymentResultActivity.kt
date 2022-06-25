package de.app.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import de.app.ui.util.toast

class PaymentResultActivity: AppCompatActivity() {

    private fun handleIntent(intent: Intent, containerViewId: Int) {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            appLinkData?.also { recipeId ->
                toast("suss")
                supportFragmentManager.commit {
                    replace(containerViewId, SubmittedResultFragment())
                }
            }
        }
    }

}