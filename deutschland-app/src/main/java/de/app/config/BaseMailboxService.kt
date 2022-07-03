package de.app.config

import android.content.Context
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import de.app.api.mail.MailMessageHeader
import de.app.api.mail.MailboxService
import de.app.config.common.AssetDataSource
import de.app.config.common.MailAsset
import de.app.config.common.MailDataSource
import java.lang.reflect.Type
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseMailboxService @Inject constructor(
    dataSource: MailHeaderDataSource
): MailboxService {

    private val accountToMessage by lazy{
        HashMap(mapOf(*dataSource.data.map {it.first to ArrayList(it.second)}.toTypedArray()))
    }

    override fun getAllMessagesForAccountId(accountId: String): List<MailMessageHeader> {
        return accountToMessage[accountId].orEmpty()
    }

    override fun removeMessagesForAccountId(accountId: String, message: MailMessageHeader) {
        accountToMessage[accountId]?.remove(message)
    }

    override fun sendMessageToAccountId(accountId: String, message: MailMessageHeader) {
        accountToMessage[accountId]?.add(message)
    }
}

@Singleton
class MailHeaderDataSource @Inject constructor(
    @ApplicationContext context: Context,
    mails: MailDataSource,
) : AssetDataSource<Pair<String, List<MailMessageHeader>>, MailByAccount>(context, "binding/mail-by-account.json") {

    private val mailById: Map<Int, MailAsset> = mails.data.associateBy { it.mailId }

    override fun map(origin: MailByAccount): Pair<String, List<MailMessageHeader>> {
        return origin.accountId to origin.mails.map {
            val app = mailById[it.mailId]!!
            MailMessageHeader(
                subject = app.subject,
                preview = app.preview,
                received = it.received,
                id = it.mailId.toString(),
                sender = app.sender
            )
        }
    }

    override fun getJsonType(): Type = object : TypeToken<List<MailByAccount>>() {}.type
}

data class MailByAccount(
    val accountId: String,
    val mails: List<MailForAccount>
)

data class MailForAccount(
    val mailId: Int,
    val received: LocalDateTime,
)