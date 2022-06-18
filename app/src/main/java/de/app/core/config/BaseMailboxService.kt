package de.app.core.config

import de.app.api.mail.MailMessage
import de.app.api.mail.MailMessageHeader
import de.app.api.mail.MailboxService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseMailboxService @Inject constructor(): MailboxService {
    override fun getAllMessagesForAccountId(accountId: String): List<MailMessageHeader> {
        TODO("Not yet implemented")
    }

    override fun getMessageById(messageId: String): Result<MailMessage> {
        TODO("Not yet implemented")
    }
}