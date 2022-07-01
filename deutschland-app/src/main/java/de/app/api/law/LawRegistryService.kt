package de.app.api.law

import java.time.LocalDate

interface LawRegistryService {
    fun getLawChanges(from: LocalDate?=null, to: LocalDate?=null): List<LawChangeHeader>
}