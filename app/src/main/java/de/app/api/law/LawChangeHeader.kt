package de.app.api.law

import de.app.data.model.FileHeader
import java.time.LocalDate
import java.util.*

data class LawChangeHeader(
    val id: UUID,
    val name: String,
    val shortDescription: String,
    val date: LocalDate
)

data class LawChangeInfo(
    val id: UUID,
    val name: String,
    val description: String,
    val attachments: List<FileHeader>,
    val date: LocalDate
)
