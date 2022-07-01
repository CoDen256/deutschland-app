package de.app.config

import de.app.api.mail.MailMessageHeader
import de.app.api.mail.MailboxService
import de.app.config.DataGenerator.Companion.accounts
import de.app.config.DataGenerator.Companion.generateMails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseMailboxService @Inject constructor(): MailboxService {

    companion object {
        val accountToMessage = HashMap(accounts.values.associate {
            it.accountId to ArrayList(generateMails(50))
        })
    }

    override fun getAllMessagesForAccountId(accountId: String): List<MailMessageHeader> {
        return accountToMessage[accountId].orEmpty()
    }

    override fun sendMessageToAccountId(accountId: String, message: MailMessageHeader) {
        accountToMessage[accountId]?.add(message)
    }
}