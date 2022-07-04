package de.app.notifications.notificator

import android.content.Context
import de.app.R
import de.app.api.account.ServiceAccount
import de.app.api.appointment.Appointment
import de.app.api.appointment.AppointmentService
import de.app.core.AccountManager
import de.app.core.SessionManager
import de.app.core.inSeparateThread
import io.karn.notify.Notify
import kotlinx.coroutines.runBlocking
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppointmentNotificator @Inject constructor() : Notificator {


    @Inject
    lateinit var service: AppointmentService

    @Inject
    lateinit var manager: SessionManager

    @Inject
    lateinit var accountManger: AccountManager
    private val alreadyNotified = ArrayList<Appointment>()

    override fun trigger(context: Context) {
        inSeparateThread {
            runBlocking {
                manager.getUsers().forEach { user ->
                    accountManger.getAccountForUser(user).onSuccess { account ->
                        val now = LocalDateTime.now()
                        val appointments = service.getAllAppointmentsByAccountId(account.accountId)
                            .filter { !alreadyNotified.contains(it) }
                            .filter {
                                it.appointment.isAfter(now) && it.appointment.isBefore(
                                    now.plusDays(
                                        7
                                    )
                                )
                            }
                        appointments.forEach {
                            notify(context, it, account)
                            alreadyNotified.add(it)
                        }
                    }
                }
            }

        }
    }

    private fun notify(
        context: Context,
        appointment: Appointment,
        account: ServiceAccount,
    ) {
        val period = Duration.between(LocalDateTime.now(), appointment.appointment )
        Notify.with(context)
            .header {
                this.headerText =
                    context.getString(R.string.notify_appointment, account.displayName)
                this.icon = R.drawable.ic_menu_appointments
            }
            .asBigText {
                this.title = context.getString(R.string.notify_appointment_title, period.toDays())
                this.bigText = context.getString(R.string.notify_appointment_text, appointment.appointment.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                this.expandedText = appointment.name
            }.show()
    }

}
