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
            val mails = ArrayList<MailMessageHeader>()
            runBlocking {
                manager.getUsers().forEach { user ->
                    accountManger.getAccountForUser(user).onSuccess {
                        val ml = service.getAllMessagesForAccountId(it.accountId)
                        if (ml.isNotEmpty()){
                            val message = ml.first()
                            notify(context, message, it)
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
                this.headerText = "Mail! for ${account.displayName}"
                this.icon = R.drawable.ic_menu_mail
            }
            .asBigText {
                this.title = "You have a new message"
                this.bigText = message.preview
                this.expandedText = message.preview
    //                                    this.conversationTitle = "You have a new mail"
    //                                    this.userDisplayName = it.displayName
    //                                    this.messages = listOf(
    //
    //                                    )
            }.show()
    }


}