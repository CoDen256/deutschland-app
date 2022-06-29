package de.app.core.config

import de.app.api.geo.GeoCategory
import de.app.api.geo.GeoSet
import de.app.api.geo.GeodataService
import de.app.core.config.DataGenerator.Companion.generateCategories
import de.app.core.config.DataGenerator.Companion.generateSets
import de.app.core.successOrElse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseGeodataService @Inject constructor(): GeodataService {

    private val sets: List<GeoSet> = generateSets(20)
    private val categories: List<GeoCategory> = generateCategories(5, 5, sets)

    override fun getAllCategories(): List<GeoCategory> {
        return categories
    }

    override fun getSetById(id: String): Result<GeoSet> {
        return sets.find { it.id == id }
            .successOrElse(IllegalArgumentException("No Set with id `$id` was found"))
    }
}