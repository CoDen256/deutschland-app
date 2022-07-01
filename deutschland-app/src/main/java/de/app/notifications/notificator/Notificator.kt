package de.app.notifications.notificator

import android.content.Context

interface Notificator {
    fun trigger(context: Context)
}

abstract class BaseNotificator<T> : Notificator {

    override fun trigger(context: Context) {
        val data = collect()
        if (data.isNotEmpty()) {
            notify(context, data)
        }
    }

    abstract fun collect(): List<T>
    abstract fun notify(context: Context, data: List<T>)

}