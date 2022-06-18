package de.app.core.config

import de.app.api.geo.GeodataService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseGeodataService @Inject constructor(): GeodataService {
}