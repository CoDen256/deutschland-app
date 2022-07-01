package de.app.api.appointment

import de.app.data.model.Address
import java.time.LocalDateTime

data class Appointment(
    val name: String,
    val description: String,
    val serviceId: String,
    val accountId: String,
    val appointment: LocalDateTime,
    val additionalInfo: String,
    val address: Address
)