package de.app.api.geo

import com.mapbox.mapboxsdk.geometry.LatLng

data class GeoCategory(
    val categoryId: String,
    val categoryName: String,
    val sets: List<GeoSetHeader>
)

open class GeoSetHeader(
    val id: String,
    val name: String,
)

class GeoSet(id: String, name: String, val positions: List<LatLng>,
): GeoSetHeader(id, name)
