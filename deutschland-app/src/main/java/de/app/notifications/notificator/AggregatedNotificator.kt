package de.app.notifications.notificator

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AggregatedNotificator @Inject constructor(): Notificator {

    @Inject lateinit var mail: MailNotificator
    @Inject lateinit var law: LawChangeNotificator
    @Inject lateinit var emergency: EmergencyNotificator
    @Inject lateinit var application: ApplicationNotificator
    @Inject lateinit var appointment: AppointmentNotificator

    override fun trigger(context: Context) {
        val notificators = listOf(
//            mail,
            law,
            emergency,
//        application,
//            appointment
            )
        notificators.forEach{ it.trigger(context) }
    }

}