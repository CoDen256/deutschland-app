package de.app.api.emergency

import de.app.data.model.Address

data class Emergency(
    val name: String,
    val description: String,
    val city: String,
    val postalCode: String,
    val country: String
)

enum class EmergecySeverity {
    HIGH, MIDDLE, LOW
}