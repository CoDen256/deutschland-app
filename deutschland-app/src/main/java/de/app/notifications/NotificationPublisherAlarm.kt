package de.app.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock

fun Context.scheduleNextAlarm(delay: Long) {
    val intent = Intent(this, RepeatedNotificatorTrigger::class.java)
    val pending = PendingIntent.getBroadcast(this, 0, intent,
        PendingIntent.FLAG_IMMUTABLE)

    val future = SystemClock.elapsedRealtime() + delay
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, future, pending)
}
