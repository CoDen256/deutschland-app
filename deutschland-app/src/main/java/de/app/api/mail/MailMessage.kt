package de.app.api.mail

import de.app.data.model.FileHeader
import java.time.Instant
import java.util.UUID

data class MailMessage(
    val subject: String,
    val content: String,
    val received: Instant,
    val removed: Boolean,
    val important: Boolean,
    val attachments: List<FileHeader>,
    val id: String
    ) {
}