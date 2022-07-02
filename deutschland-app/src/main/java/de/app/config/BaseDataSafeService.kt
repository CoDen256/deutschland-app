package de.app.config

import de.app.api.safe.DataSafeService
import de.app.config.DataGenerator.Companion.accounts
import de.app.config.DataGenerator.Companion.generateDocuments
import de.app.data.model.FileHeader
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random.Default.nextInt

@Singleton
class BaseDataSafeService @Inject constructor(
    source: DataSaveDataSource

): DataSafeService {

    private val documents = HashMap(accounts.values
        .associate { it.accountId to ArrayList(generateDocuments(nextInt(10))) })

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
class DataSaveDataSource @Inject constructor(dataSource: FileHeaderAssetDataSource){
    val citizenData by lazy {
        dataSource.data[0].`citizen-files`
    }

    val companyData by lazy {
        dataSource.data[0].`company-files`
    }
}