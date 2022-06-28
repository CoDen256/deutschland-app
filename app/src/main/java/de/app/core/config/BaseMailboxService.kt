package de.app.core.config

import de.app.api.mail.MailMessage
import de.app.api.mail.MailMessageHeader
import de.app.api.mail.MailboxService
import de.app.core.config.DataGenerator.Companion.generateMails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseMailboxService @Inject constructor(): MailboxService {
    override fun getAllMessagesForAccountId(accountId: String): List<MailMessageHeader> {
        return generateMails(50)
    }

    override fun getMessageById(messageId: String): Result<MailMessage> {
        TODO("Not yet implemented")
    }
}