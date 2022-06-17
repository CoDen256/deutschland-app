package de.app.ui.user.select

import androidx.lifecycle.ViewModel
import de.app.core.SessionManager
import de.app.data.model.UserHeader
import javax.inject.Inject

class SelectUserViewModel @Inject constructor(private val manager: SessionManager) : ViewModel() {

    suspend fun getAccounts(): List<UserHeader> {
        return manager.getUsers()
    }
}