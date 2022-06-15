package de.app.api.law

import java.time.LocalDate
import java.util.*

data class LawChangeHeader(
    val id: UUID,
    val name: String,
    val shortDescription: String,
    val date: LocalDate
)
