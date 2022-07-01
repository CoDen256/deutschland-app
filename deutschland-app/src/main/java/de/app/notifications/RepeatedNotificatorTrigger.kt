package de.app.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import de.app.notifications.notificator.Notificator
import javax.inject.Inject

@AndroidEntryPoint
class RepeatedNotificatorTrigger: BroadcastReceiver() {

    @Inject lateinit var notificator: Notificator

    companion object {
        const val INTERVAL = 1000L*15*1
        const val repeat = true
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let { ctx ->
            notificator.trigger(ctx)
            if (repeat) { ctx.scheduleNextAlarm(INTERVAL) }
        }
    }
}