package de.app.data.model.service.submit

import de.app.data.model.service.AdministrativeService

data class SubmittedForm(
    val service: AdministrativeService,
    val submittedForm: List<SubmittedField>
)
