package de.app.data.model.service

import java.util.*

data class AdministrativeService(
    val id: UUID,
    val name: String,
    val description: String,
    val apiEndpoint: String,
    val address: Address
)
