package de.app.data.model.adminservice

import java.util.*

data class AdministrativeServiceProvider(
    val id: UUID,
    val name: String,
    val description: String,
    val address: String
)
