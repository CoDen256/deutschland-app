package de.app.data.model.service

data class SubmittedForm(
    val service: AdministrativeService,
    val submittedForm: List<SubmittedField>
)
