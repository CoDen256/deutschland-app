package de.app.data.model.service.form

import de.app.data.model.service.AdministrativeService

data class Form(
    val name: String,
    val description: String,
    val service: AdministrativeService,
    val fields: List<Field>,
    val paymentRequired: Boolean
)
