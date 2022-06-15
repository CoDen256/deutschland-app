package de.app.ui.account.select

import androidx.lifecycle.ViewModel
import de.app.core.SessionManager
import javax.inject.Inject

class SelectAccountViewModel @Inject constructor(
    private val manager: SessionManager): ViewModel() {

    fun getAccounts(): List<AccountHeader> {
        return manager.getAccounts()
    }
}