package de.app.api.geo

import com.mapbox.mapboxsdk.geometry.LatLng

data class City(
    val city: String,
    val pos: LatLng,
    val country: String,
    val adminName: String,
)

data class GeoCategory(
    val categoryId: String,
    val categoryName: String,
    val sets: List<GeoSetHeader>
)

data class FullGeoCategory(
    val categoryId: String,
    val categoryName: String,
    val sets: List<GeoSet>
)

open class GeoSetHeader(
    val id: String,
    val name: String,
)

class GeoSet(id: String, name: String, val positions: List<LatLng>,
): GeoSetHeader(id, name)
