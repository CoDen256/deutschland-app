package de.app.core.config

import de.app.api.safe.DataSafeService
import de.app.core.config.DataGenerator.Companion.generateDocuments
import de.app.data.model.FileHeader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseDataSafeService @Inject constructor(): DataSafeService {
    val documents = generateDocuments(50)

    override fun getAllDocumentsForAccountId(accountId: String): List<FileHeader> {
        return documents
    }
}