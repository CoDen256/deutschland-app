package de.app.ui.components

import androidx.viewbinding.ViewBinding
import de.app.api.account.AccountInfo
import de.app.core.AccountManager
import de.app.ui.user.LoginActivity
import de.app.ui.util.runActivity
import de.app.ui.util.toast
import javax.inject.Inject


abstract class AccountAwareFragment<B: ViewBinding>: SimpleFragment<B>() {

    @Inject lateinit var accountManager: AccountManager

    override fun setup() {
        val account = accountManager.getCurrentAccount().getOrElse {
            activity?.toast("Failed to get current account: "+it.message)
            activity?.runActivity(LoginActivity::class.java)
            return
        }
        setup(account)
    }

    abstract fun setup(account: AccountInfo)

}