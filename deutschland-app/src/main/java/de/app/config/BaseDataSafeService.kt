package de.app.config

import android.content.Context
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import de.app.api.safe.DataSafeService
import de.app.config.common.FileHeaderAsset
import de.app.config.common.FileHeaderDataSource
import de.app.data.model.FileHeader
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseDataSafeService @Inject constructor(source: DatasafeDataSource): DataSafeService {

    private val documents = HashMap(mapOf(*source.data.map {
        it.first to ArrayList(it.second)
    }.toTypedArray()))

    override fun getAllDocumentsForAccountId(accountId: String): List<FileHeader> {
        return documents[accountId] ?: emptyList()
    }

    override fun upload(fileHeader: FileHeader, accountId: String) {
        documents[accountId]?.add(fileHeader)
    }

    override fun remove(fileHeader: FileHeader, accountId: String) {
        documents[accountId]?.remove(fileHeader)
    }
}

@Singleton
class DatasafeDataSource @Inject constructor(@ApplicationContext private val context: Context, fileHeaders: FileHeaderDataSource, ) :
    AssetDataSource<Pair<String, List<FileHeader>>, DatasafeBinding>(context, "binding/datasafe.json") {

    private val fileHeadersById: Map<Int, FileHeaderAsset> by lazy {
        fileHeaders.data.associateBy { it.id }
    }
    override fun map(origin: DatasafeBinding): Pair<String, List<FileHeader>> {
        return origin.accountId to origin.fileIds.mapNotNull { fileHeadersById[it] }.map { it.map() }
    }

    override fun getJsonType(): Type = object : TypeToken<List<DatasafeBinding>>() {}.type
}

data class DatasafeBinding(
    val accountId: String,
    val fileIds: List<Int>
)