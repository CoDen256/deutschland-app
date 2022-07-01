package de.app.api.law

import java.time.LocalDate

data class LawChangeHeader(
    val id: String,
    val name: String,
    val shortDescription: String,
    val date: LocalDate
)
