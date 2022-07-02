package de.app.api.mail

import java.time.LocalDateTime

data class MailMessageHeader(
    val subject: String,
    val received: LocalDateTime,
    val id: String,
    val preview: String = ""
) {
}