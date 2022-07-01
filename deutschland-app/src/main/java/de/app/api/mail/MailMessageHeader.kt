package de.app.api.mail

import java.time.Instant
import java.util.*

data class MailMessageHeader(
    val subject: String,
    val received: Instant,
    val id: String,
    val preview: String = ""
) {
}