package de.app.api.safe

import de.app.data.model.FileHeader

interface DataSafeService {
    fun getAllDocumentsForAccountId(accountId: String): List<FileHeader>
    fun upload(fileHeader: FileHeader, accountId: String)
}