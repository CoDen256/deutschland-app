package de.app.api.service

import de.app.data.model.Address
import java.util.*

data class AdministrativeService(
    val id: String,
    val name: String,
    val description: String,
    val apiEndpoint: String,
    val address: Address
)
