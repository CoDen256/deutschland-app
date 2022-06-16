package de.app.ui.account.select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.app.core.SessionManager
import de.app.data.model.AccountHeader
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectAccountViewModel @Inject constructor(
    private val manager: SessionManager
) : ViewModel() {

    suspend fun getAccounts(): List<AccountHeader> {
        return manager.getAccounts()
    }
}