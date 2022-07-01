package de.app.notifications.notificator

import android.content.Context
import de.app.api.mail.MailMessageHeader
import de.app.config.DataGenerator.Companion.generateMails
import io.karn.notify.Notify
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MailNotificator @Inject constructor(): BaseNotificator<MailMessageHeader>() {

    override fun collect(): List<MailMessageHeader> {
        return generateMails(10)
    }

    override fun notify(context: Context, data: List<MailMessageHeader>) {
        Notify.with(context)
            .content {
                title = "You have received a new mail!"
                this.text = data.first().subject
            }.show()
    }


}