package de.app.config

import android.content.Context
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import de.app.api.law.LawChangeHeader
import de.app.api.law.LawRegistryService
import de.app.core.range
import java.lang.reflect.Type
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseLawRegistryService @Inject constructor(private val source: LawAssetDataSource): LawRegistryService {
    private val lawChangeHeaders: MutableList<LawChangeHeader> by lazy {
        ArrayList(source.data)
    }

    override fun getLawChanges(from: LocalDate?, to: LocalDate?): List<LawChangeHeader> {
        return lawChangeHeaders.filter { it.date in range(from, to) }
    }
}

@Singleton
class LawAssetDataSource @Inject constructor(@ApplicationContext private val context: Context):
    AssetDataSource<LawChangeHeader, LawChangeAsset>(context, "laws.json") {
    override fun map(origin: LawChangeAsset): LawChangeHeader {
        return LawChangeHeader(origin.id, origin.name, origin.description, origin.date)
    }

    override fun getJsonType(): Type = object : TypeToken<List<LawChangeAsset>>() {}.type
}

data class LawChangeAsset(
    val id: Int,
    val date: LocalDate,
    val name: String,
    val description: String
)