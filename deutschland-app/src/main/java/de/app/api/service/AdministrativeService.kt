package de.app.api.service

import de.app.data.model.Address
import java.util.*

data class AdministrativeService(
    val email: String,
    val id: String,
    val name: String,
    val description: String,
    val apiEndpoint: String,
    val address: Address,
    val type: ServiceType
)

enum class ServiceType{
    CITIZEN, COMPANY, COMMON
}

