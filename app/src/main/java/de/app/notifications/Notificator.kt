package de.app.notifications

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import de.app.R
import io.karn.notify.Notify

class Notificator(private val context: Context) {


    fun sendEmergencyNotification(text: String){
        Notify.with(context)
            .content {
                title = "Emergency!"
                this.text = text
            }.show()
    }

    fun sendNewMailNotification(text: String){
        Notify.with(context)
            .content {
                title = "You have received a new mail!"
                this.text = text
            }.show()
    }

    fun sendLawChangeNotification(text: String){
        Notify.with(context)
            .content {
                title = "Law Registry"
                this.text = text
            }.show()
    }

}