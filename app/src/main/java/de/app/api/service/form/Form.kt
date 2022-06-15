package de.app.api.service.form

import de.app.api.service.AdministrativeService

data class Form(
    val name: String,
    val description: String,
    val service: AdministrativeService,
    val fields: List<Field>,
    val paymentRequired: Boolean
)
