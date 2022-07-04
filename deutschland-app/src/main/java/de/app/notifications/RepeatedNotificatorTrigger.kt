package de.app.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import de.app.notifications.notificator.Notificator
import io.karn.notify.Notify
import kotlinx.coroutines.*
import java.time.Duration
import javax.inject.Inject

@AndroidEntryPoint
class RepeatedNotificatorTrigger: BroadcastReceiver() {

    @Inject lateinit var notificator: Notificator

    companion object {
        const val LAW_DELAY_SECONDS = 20L
        const val INTERVAL = 1000L*15*1
        const val INITIAL_DELAY = 0L
        const val repeat = true
        const val debug = true
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let { ctx ->
            if (debug) Notify.with(context).content { text ="Heartbeat" }.show()
            notificator.trigger(ctx)
            if (repeat) { ctx.scheduleNextAlarm(INTERVAL) }
        }
    }
}