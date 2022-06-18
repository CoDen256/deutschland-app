package de.app.api.applications

data class Application (
    val serviceId: String,
    val accountId: String,
    val name: String,
    val description: String,
    val status: ApplicationStatus
)

enum class ApplicationStatus(val order: Int) {
    RECEIVED(0),
    VERIFICATION(1),
    PROCESSING(2),
    DONE(3), REJECTED(3)
}
