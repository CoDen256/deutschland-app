package de.app.core.config

import de.app.api.signature.SignatureService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseSignatureService @Inject constructor(): SignatureService {
}