package de.app.api.service.form

data class Form(
    val serviceId: String,
    val fields: List<Field>,
    val paymentRequired: Boolean
)
