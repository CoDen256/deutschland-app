package de.app.core.config

import de.app.api.signature.SignatureService
import de.app.data.model.FileHeader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseSignatureService @Inject constructor(): SignatureService {
    override fun signFile(fileHeader: FileHeader): FileHeader {
        return FileHeader(
            uri = fileHeader.uri,
            name = "Signed-"+fileHeader.name,
            mimeType = fileHeader.mimeType
        )
    }
}