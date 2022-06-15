package de.app.api.mail

import java.time.Instant
import java.util.*

data class MailMessageHeader(
    val subject: String,
    val received: Instant,
    val removed: Boolean,
    val important: Boolean,
    val id: UUID,
    val preview: String = ""
) {
}