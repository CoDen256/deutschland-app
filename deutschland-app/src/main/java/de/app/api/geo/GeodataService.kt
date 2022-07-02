package de.app.api.geo


interface GeodataService {
    fun getAllCategories(): List<GeoCategory>
    fun getSetById(id: String): Result<GeoSet>
    fun getAllCities(): List<City>
}