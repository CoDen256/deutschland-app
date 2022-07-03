package de.app.api.mail

interface MailboxService {
    fun getAllMessagesForAccountId(accountId: String): List<MailMessageHeader>
    fun removeMessagesForAccountId(accountId: String, message: MailMessageHeader)
    fun sendMessageToAccountId(accountId: String, message: MailMessageHeader)
}