package de.app.data.model.adminservice

data class SubmittedForm(
    val service: AdministrativeService,
    val submittedForm: List<SubmittedField>
)
