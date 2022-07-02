package de.app.api.emergency

import java.time.LocalDateTime

data class Emergency(
    val id: Int,
    val name: String,
    val description: String,
    val city: String,
    val postalCode: String,
    val country: String,
    val dateTime: LocalDateTime,
    val severity: EmergencySeverity
)

enum class EmergencySeverity {
    HIGH, MEDIUM, LOW
}