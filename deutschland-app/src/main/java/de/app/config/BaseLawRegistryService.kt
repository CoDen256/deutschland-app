package de.app.config

import android.content.Context
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import de.app.api.law.LawChange
import de.app.api.law.LawRegistryService
import de.app.core.range
import java.lang.reflect.Type
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseLawRegistryService @Inject constructor(private val source: LawAssetDataSource) :
    LawRegistryService {

    private val secondDelay = 10L
    private val lawChanges: MutableList<LawChange> by lazy {
        fakeFirstLawAsNew(source.data)
    }

    override fun getLawChanges(from: LocalDateTime?, to: LocalDateTime?): List<LawChange> {
        return lawChanges.filter { it.date in range(from, to) }
    }

    private fun fakeFirstLawAsNew(changes: List<LawChange>): MutableList<LawChange> =
        ArrayList(changes).apply {
            replaceAll {
                if (it.id == 1) LawChange(it.id, it.name, it.description, LocalDateTime.now().plusSeconds(secondDelay))
                else it
            }
        }
}

@Singleton
class LawAssetDataSource @Inject constructor(@ApplicationContext private val context: Context) :
    AssetDataSource<LawChange, LawChangeAsset>(context, "laws.json") {
    override fun map(origin: LawChangeAsset): LawChange {
        return LawChange(origin.id, origin.name, origin.description, origin.date)
    }

    override fun getJsonType(): Type = object : TypeToken<List<LawChangeAsset>>() {}.type
}

data class LawChangeAsset(
    val id: Int,
    val date: LocalDateTime,
    val name: String,
    val description: String
)