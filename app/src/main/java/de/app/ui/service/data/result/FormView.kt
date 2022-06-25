package de.app.ui.service.data.result

/**
 * User details post authentication that is exposed to the UI
 */
data class FormView(
    val applicationId: String,
    val accountId: String,
    val accountDisplayName: String,
    val serviceName: String,
    val sentDate: String
)


