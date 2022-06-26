package de.app.notifications.notificator

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AggregatedNotificator @Inject constructor(): Notificator {

    @Inject lateinit var mail: MailNotificator
    @Inject lateinit var law: LawChangeNotificator

    override fun trigger(context: Context) {
        val notificators = listOf(mail, law)
        notificators.forEach{ it.trigger(context) }
    }

}