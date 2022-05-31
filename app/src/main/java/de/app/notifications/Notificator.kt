package de.app.notifications

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import de.app.R

class Notificator(private val context: Context) {


    fun buildNotification(channelId: String): NotificationCompat.Builder{
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Fake Notification")
            .setContentText("Fake Fake Fake")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    fun sendNotification(notificationId: Int, builder: NotificationCompat.Builder){
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }

}