package de.app.config

import android.content.Context
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import de.app.data.model.FileHeader
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton




@Singleton
class FileHeaderAssetDataSource @Inject constructor(@ApplicationContext private val context: Context) :
    AssetDataSource<FileHeaderCollectionAsset, FileHeaderCollectionAsset>(context, "documents.json") {
    override fun map(origin: FileHeaderCollectionAsset): FileHeaderCollectionAsset {
        return origin
    }

    override fun getJsonType(): Type = object : TypeToken<List<FileHeaderCollectionAsset>>() {}.type
}

data class FileHeaderCollectionAsset(
    val `citizen-files`: List<FileHeader>,
    val `company-files`: List<FileHeader>,
    val `admin-service-files`: List<FileHeader>,
)