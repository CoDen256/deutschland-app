package de.app.notifications.notificator

import android.content.Context
import de.app.api.applications.ApplicationService
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
class ApplicationNotificator @Inject constructor(): Notificator {

    private var lastFetch: LocalDateTime = LocalDateTime.now()

    @Inject
    lateinit var service: ApplicationService
    @Inject lateinit var manager: SessionManager
    @Inject lateinit var accountManger: AccountManager


    override fun trigger(context: Context) {
        inSeparateThread {
            val mails = ArrayList<MailMessageHeader>()
            runBlocking {
                manager.getUsers().forEach { user ->
                    accountManger.getAccountForUser(user).onSuccess {
                        val ml = service.getAllApplicationsByAccountId(it.accountId)
                        if (ml.isNotEmpty()){
                            Notify.with(context)
                                .asBigText {
                                    this.title = "You have a new message"
                                    this.bigText = ml.first().description
                                    this.expandedText = ml.first().description
//                                    this.conversationTitle = "You have a new mail"
//                                    this.userDisplayName = it.displayName
//                                    this.messages = listOf(
//
//                                    )
                                }.show()
                        }
                    }
                }
                lastFetch = LocalDateTime.now()
            }

        }
    }

}
