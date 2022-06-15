package de.app.ui.account.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.app.core.SessionManager
import de.app.data.model.AccountHeader
import java.util.Collections.list
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import javax.inject.Inject

class SelectAccountViewModel @Inject constructor(
    private val manager: SessionManager): ViewModel() {

    fun getAccounts(): LiveData<List<AccountHeader>> {
        val data = MutableLiveData(listOf<AccountHeader>())
        CompletableFuture.runAsync {
            val accounts = manager.getAccounts()
            data.value = accounts
        }
        return data;
    }
}