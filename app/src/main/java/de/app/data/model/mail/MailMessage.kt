package de.app.data.model.mail

import java.time.Instant
import java.util.UUID

data class MailMessage(
    val subject: String,
    val content: String,
    val received: Instant,
    val removed: Boolean,
    val important: Boolean,
    val attachments: List<AttachmentHeader>,
    val id: UUID
    ) {
}