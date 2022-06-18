package de.app.core.config

import de.app.api.law.LawChangeHeader
import de.app.api.law.LawChangeInfo
import de.app.api.law.LawRegistryService
import de.app.core.config.DataGenerator.Companion.generateLawChanges
import de.app.core.range
import de.app.core.successOrElse
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseLawRegistryService @Inject constructor(): LawRegistryService {
    private val lawChanges: List<LawChangeInfo> = generateLawChanges(40)
    private val lawChangeHeaders = lawChanges.map {
        LawChangeHeader(it.id, it.name, it.shortDescription, it.date)
    }


    override fun getLawChanges(from: LocalDate?, to: LocalDate?): List<LawChangeHeader> {
        return lawChangeHeaders.filter { it.date in range(from, to) }
    }

    override fun getLawChangeById(id: String): Result<LawChangeInfo> {
        return lawChanges.find{ it.id == id }.successOrElse()
    }
}