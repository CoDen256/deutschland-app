package de.app.api.signature

import de.app.data.model.FileHeader

interface SignatureService {
    fun signFile(fileHeader: FileHeader): FileHeader
}