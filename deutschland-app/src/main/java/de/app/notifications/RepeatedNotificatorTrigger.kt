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
        const val EMERGENCY_DELAY_SECONDS = 40L
        const val APPLICATION_DELAY_SECONDS = 15L
        const val MAIL_DELAY_SECONDS = 15L
        const val INTERVAL = 1000L*15*1
        const val INITIAL_DELAY = 0L
        const val repeat = 5
        const val debug = true
        private var current = 1
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let { ctx ->
            if (debug) Notify.with(context).content { text ="Heartbeat: $current/$repeat" }.show()
            notificator.trigger(ctx)
            if (current++<repeat) { ctx.scheduleNextAlarm(INTERVAL) }
        }
    }
}