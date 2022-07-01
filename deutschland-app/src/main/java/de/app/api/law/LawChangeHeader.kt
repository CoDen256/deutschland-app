package de.app.api.law

import de.app.data.model.FileHeader
import java.time.LocalDate
import java.util.*

data class LawChangeHeader(
    val id: String,
    val name: String,
    val shortDescription: String,
    val date: LocalDate
)

data class LawChangeInfo(
    val id: String,
    val name: String,
    val shortDescription: String,
    val content: String,
    val attachments: List<FileHeader>,
    val date: LocalDate
)
