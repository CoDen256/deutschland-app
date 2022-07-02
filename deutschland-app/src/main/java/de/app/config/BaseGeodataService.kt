package de.app.config

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.mapbox.mapboxsdk.geometry.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import de.app.api.geo.*
import de.app.config.common.AssetDataSource
import de.app.core.successOrElse
import java.lang.reflect.Type
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseGeodataService @Inject constructor(
    private val cityDataSource: GeoCityAssetDataSource,
    private val source: GeoCategoryAssetDataSource
): GeodataService {

    private val fullCategories by lazy {
        source.data
    }

    private val reducedCategories by lazy {
        fullCategories.map {
            GeoCategory(it.categoryId, it.categoryName, it.sets)
        }
    }

    private val sets by lazy {
        fullCategories.flatMap { it.sets }
    }

    private val cities by lazy {
        cityDataSource.data
    }
    override fun getAllCategories(): List<GeoCategory> {
        return reducedCategories
    }

    override fun getSetById(id: String): Result<GeoSet> {
        return sets.find { it.id == id }
            .successOrElse(IllegalArgumentException("No Set with id `$id` was found"))
    }

    override fun getAllCities(): List<City> = cities
}

@Singleton
class GeoCategoryAssetDataSource @Inject constructor(@ApplicationContext private val context: Context) :
    AssetDataSource<FullGeoCategory, GeoCategoryAsset>(context, "common/geo.json") {
    override fun map(origin: GeoCategoryAsset): FullGeoCategory {
        return FullGeoCategory(
            categoryId = UUID.randomUUID().toString(),
            categoryName = origin.name,
            sets = origin.subcategories.map{mapSet(it)}
        )
    }

    private fun mapSet(origin: GeoSetAsset): GeoSet {
        return GeoSet(
            id = UUID.randomUUID().toString(),
            name=origin.name,
            positions = origin.positions.map { LatLng(it.lat, it.long) }
        )
    }


    override fun getJsonType(): Type = object : TypeToken<List<GeoCategoryAsset>>() {}.type
}

data class GeoCategoryAsset(
    val name: String,
    val subcategories: List<GeoSetAsset>
)

data class GeoSetAsset(
    val name: String,
    val positions: List<PositionAsset>
)

data class PositionAsset(
    val lat: Double,
    val long: Double
)

@Singleton
class GeoCityAssetDataSource @Inject constructor(@ApplicationContext private val context: Context) :
    AssetDataSource<City, CityAsset>(context, "common/cities.json") {
    override fun map(origin: CityAsset): City {
        return City(
            city = origin.city,
            country = origin.country,
            pos = LatLng(origin.lat, origin.lng),
            adminName = origin.admin_name
        )
    }

    override fun getJsonType(): Type = object : TypeToken<List<CityAsset>>() {}.type
}


data class CityAsset(
    val city: String,
    val lat: Double,
    val lng: Double,
    val country: String,
    val iso2: String,
    val admin_name: String,
    val capital: String,
    val population: String,
    val population_proper: String,
)
