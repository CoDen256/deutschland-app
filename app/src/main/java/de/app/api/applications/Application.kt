package de.app.api.applications

import java.time.LocalDateTime

data class Application (
    val serviceId: String,
    val accountId: String,
    val name: String,
    val description: String,
    val status: ApplicationStatus,
    val applicationDate: LocalDateTime
)

enum class ApplicationStatus(val order: Int) {
    SENT(0),
    VERIFICATION(1),
    PROCESSING(2),
    DONE(3), REJECTED(3)
}
