package de.app.core.config

import de.app.api.safe.DataSafeService
import de.app.core.config.DataGenerator.Companion.accounts
import de.app.core.config.DataGenerator.Companion.generateDocuments
import de.app.data.model.FileHeader
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random.Default.nextInt

@Singleton
class BaseDataSafeService @Inject constructor(): DataSafeService {
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