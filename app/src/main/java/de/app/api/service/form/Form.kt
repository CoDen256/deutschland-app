package de.app.api.service.form

data class Form(
    val fields: List<Field>,
    val paymentRequired: Boolean
)
