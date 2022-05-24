package de.app.data.model.service.form

import de.app.data.model.service.AdministrativeService

data class ApplicationForm(
    val name: String,
    val description: String,
    val service: AdministrativeService,
    val form: List<FormField>
)
