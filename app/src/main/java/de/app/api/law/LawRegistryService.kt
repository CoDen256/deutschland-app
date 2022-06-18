package de.app.api.law

import java.time.LocalDate

interface LawRegistryService {
    fun getLawChanges(from: LocalDate, to: LocalDate): List<LawChangeHeader>
    fun getLawChangeById(id: String): Result<LawChangeInfo>
}