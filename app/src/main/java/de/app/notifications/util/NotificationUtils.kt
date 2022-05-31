package de.app.notifications.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context


fun Context.createNotificationChannel(id: String, name: String, description: String) {
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(id, name, importance).apply {
        this.description = description
    }
    val notificationManager: NotificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}