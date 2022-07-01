package de.app.api.emergency

import java.time.LocalDateTime

data class Emergency(
    val id: String,
    val name: String,
    val description: String,
    val city: String,
    val postalCode: String,
    val country: String,
    val date: LocalDateTime,
    val severity: EmergecySeverity
)

enum class EmergecySeverity {
    HIGH, MIDDLE, LOW
}