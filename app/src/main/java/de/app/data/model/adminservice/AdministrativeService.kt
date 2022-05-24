package de.app.data.model.adminservice

import java.util.*

data class AdministrativeService(
    val id: UUID,
    val name: String,
    val description: String,
    val apiEndpoint: String,
)
