package de.app.data.model.adminservice

sealed interface ApplicationField

data class TextField(
    val label: String,
    val hint: String?,
    val name: String
): ApplicationField