package de.app.notifications.notificator

import android.content.Context
import de.app.R
import de.app.api.account.ServiceAccount
import de.app.api.appointment.Appointment
import de.app.api.appointment.AppointmentService
import de.app.api.mail.MailMessageHeader
import de.app.core.AccountManager
import de.app.core.SessionManager
import de.app.core.inSeparateThread
import io.karn.notify.Notify
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppointmentNotificator @Inject constructor(): Notificator {

    private var lastFetch: LocalDateTime = LocalDateTime.now()

    @Inject
    lateinit var service: AppointmentService
    @Inject lateinit var manager: SessionManager
    @Inject lateinit var accountManger: AccountManager


    override fun trigger(context: Context) {
        inSeparateThread {
            val mails = ArrayList<MailMessageHeader>()
            runBlocking {
                manager.getUsers().forEach { user ->
                    accountManger.getAccountForUser(user).onSuccess {
                        val ml = service.getAllAppointmentsByAccountId(it.accountId)
                        if (ml.isNotEmpty()){
                            val appointment = ml.first()
                            notify(context, appointment, it)
                        }
                    }
                }
                lastFetch = LocalDateTime.now()
            }

        }
    }

    private fun notify(
        context: Context,
        appointment: Appointment,
        account: ServiceAccount,
    ) {
        Notify.with(context)
            .header {
                this.headerText = "Appointment! for ${account.displayName}"
                this.icon = R.drawable.ic_menu_appointments
            }
            .asBigText {
                this.title = "You have a new message"
                this.bigText = appointment.description
                this.expandedText = appointment.description
    //                                    this.conversationTitle = "You have a new mail"
    //                                    this.userDisplayName = it.displayName
    //                                    this.messages = listOf(
    //
    //                                    )
            }.show()
    }

}
