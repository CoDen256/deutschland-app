package de.app.api.law

import java.time.LocalDateTime

data class LawChange(
    val id: Int,
    val name: String,
    val description: String,
    val date: LocalDateTime
)
