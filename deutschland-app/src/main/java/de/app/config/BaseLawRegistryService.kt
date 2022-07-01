package de.app.config

import de.app.api.law.LawChangeHeader
import de.app.api.law.LawRegistryService
import de.app.core.range
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseLawRegistryService @Inject constructor(private val source: LawAssetDataSource): LawRegistryService {
    private val lawChangeHeaders = source.getAll().map {
        LawChangeHeader(it.id, it.name, it.shortDescription, it.date)
    }

    override fun getLawChanges(from: LocalDate?, to: LocalDate?): List<LawChangeHeader> {
        // TODO: run in separate thread the update of laws?
        source.getAll()
        return lawChangeHeaders.filter { it.date in range(from, to) }
    }
}

@Singleton
class LawAssetDataSource @Inject constructor(): AssetDataSource<LawChangeHeader, LawChangeAsset>()


data class LawChangeAsset(
    val id: Int,
    val date: LocalDate,
    val name: String,
    val description: String
)