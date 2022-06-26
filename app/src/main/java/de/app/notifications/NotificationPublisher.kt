package de.app.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import de.app.core.SessionManager
import javax.inject.Inject

@AndroidEntryPoint
class NotificationPublisher: BroadcastReceiver() {

    @Inject lateinit var sessionManager: SessionManager


    companion object {
        const val INTERVAL = 1000L*30*1
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            Notificator(it).sendNewMailNotification("hello")

//            it.scheduleNextAlarm(INTERVAL)
        }
    }
}