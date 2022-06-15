package de.app.api.service

import java.util.*

data class AdministrativeServiceProvider(
    val id: UUID,
    val name: String,
    val description: String,
    val address: String
)
