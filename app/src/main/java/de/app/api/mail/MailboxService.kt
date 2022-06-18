package de.app.api.mail

interface MailboxService {
    fun getAllMessagesForAccountId(accountId: String): List<MailMessageHeader>
    fun getMessageById(messageId: String): Result<MailMessage>
}