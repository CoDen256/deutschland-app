package de.app.api.law

import java.time.LocalDateTime

interface LawRegistryService {
    fun getLawChanges(from: LocalDateTime?=null, to: LocalDateTime?=null): List<LawChange>
}