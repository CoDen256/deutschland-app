package de.app.core.config

import de.app.api.safe.DataSafeService
import de.app.data.model.FileHeader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseDataSafeService @Inject constructor(): DataSafeService {
    override fun getAllDocumentsForAccountId(accountId: String): List<FileHeader> {
        TODO("Not yet implemented")
    }
}