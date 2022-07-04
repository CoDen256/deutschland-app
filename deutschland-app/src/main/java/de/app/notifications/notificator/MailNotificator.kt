package de.app.notifications.notificator

import android.content.Context
import de.app.R
import de.app.api.account.ServiceAccount
import de.app.api.mail.MailMessageHeader
import de.app.api.mail.MailboxService
import de.app.core.AccountManager
import de.app.core.SessionManager
import de.app.core.inSeparateThread
import io.karn.notify.Notify
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MailNotificator @Inject constructor(): Notificator {


    private var lastFetch: LocalDateTime = LocalDateTime.now()

    @Inject
    lateinit var service: MailboxService
    @Inject lateinit var manager: SessionManager
    @Inject lateinit var accountManger: AccountManager


    override fun trigger(context: Context) {
        inSeparateThread {
            runBlocking {
                manager.getUsers().forEach { user ->
                    accountManger.getAccountForUser(user).onSuccess { account ->
                        val mails = service.getAllMessagesForAccountId(account.accountId)
                            .filter { it.received.isAfter(lastFetch) &&
                            it.received.isBefore(LocalDateTime.now())}
                        mails.forEach {
                            notify(context, it, account)

                        }
                    }
                }
                lastFetch = LocalDateTime.now()
            }

        }
    }

    private fun notify(context: Context, message: MailMessageHeader, account: ServiceAccount) {
        Notify.with(context)
            .header {
                this.headerText = context.getString(R.string.notify_mail, account.displayName)
                this.icon = R.drawable.ic_menu_mail
            }
            .asBigText {
                this.title = context.getString(R.string.notify_mail_title)
                this.bigText = context.getString(R.string.notify_mail_text, message.sender, message.received.format(
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")))
                this.expandedText = message.preview
            }.show()
    }


}