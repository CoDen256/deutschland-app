package de.app.notifications.notificator

import android.content.Context
import de.app.R
import de.app.api.account.ServiceAccount
import de.app.api.applications.Application
import de.app.api.applications.ApplicationService
import de.app.core.AccountManager
import de.app.core.SessionManager
import de.app.core.inSeparateThread
import io.karn.notify.Notify
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationNotificator @Inject constructor() : Notificator {

    private var lastFetch: LocalDateTime = LocalDateTime.now()

    @Inject
    lateinit var service: ApplicationService
    @Inject
    lateinit var manager: SessionManager
    @Inject
    lateinit var accountManger: AccountManager


    override fun trigger(context: Context) {
        inSeparateThread {
            runBlocking {
                manager.getUsers().forEach { user ->
                    accountManger.getAccountForUser(user).onSuccess { account ->
                        val applications = service.getAllApplicationsByAccountId(account.accountId)
                            .filter { it.applicationDate.isAfter(lastFetch) &&
                                    it.applicationDate.isBefore(LocalDateTime.now())
                            }
                        applications.forEach {
                            notify(context, it, account)
                        }
                    }
                }
                lastFetch = LocalDateTime.now()
            }

        }
    }

    private fun notify(context: Context, application: Application, account: ServiceAccount) {
        Notify.with(context)
            .header {
                this.headerText =
                    context.getString(R.string.notify_application, account.displayName)
                this.icon = R.drawable.ic_menu_applications
            }
            .asBigText {
                this.bigText = context.getString(
                    R.string.notify_application_text,
                    application.status.toString(context)
                )
                this.expandedText = context.getString(R.string.notify_application_title) + ": ${application.name}"
                this.title = context.getString(R.string.notify_application_title)
            }
            .show()
    }

}
