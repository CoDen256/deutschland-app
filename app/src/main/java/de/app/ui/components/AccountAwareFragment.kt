package de.app.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import de.app.api.account.ServiceAccount
import de.app.core.AccountManager
import de.app.ui.user.LoginActivity
import de.app.ui.util.runActivity
import de.app.ui.util.toast
import javax.inject.Inject


abstract class AccountAwareFragment<B: ViewBinding>: SimpleFragment<B>() {

    @Inject lateinit var accountManager: AccountManager
    protected lateinit var account: ServiceAccount

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        account = accountManager.getCurrentAccount().getOrElse {
            activity?.toast("Failed to get current account: "+it.message)
            activity?.runActivity(LoginActivity::class.java)
            return root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}