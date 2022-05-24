package de.app.data.model.adminservice

data class ApplicationForm(
    val name: String,
    val description: String,
    val service: AdministrativeService,
    val form: List<ApplicationField>
)
