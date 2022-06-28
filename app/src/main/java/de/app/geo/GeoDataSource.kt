package de.app.geo

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.app.core.success
import java.io.IOException

object GeoDataSource {

    private val cities: MutableList<City> = ArrayList()

    private suspend fun getJsonDataFromAsset(context: Context, fileName: String): Result<String> {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }.success()
        } catch (ioException: IOException) {
            Result.failure(ioException)
        }
    }

    suspend fun init(context: Context, fileName: String) {
        getJsonDataFromAsset(context, fileName).onSuccess {
            val gson = Gson()
            val listCity = object : TypeToken<List<City>>() {}.type
            cities.addAll(gson.fromJson(it, listCity))
        }
    }

    fun requestCities(): List<City>{
        return cities
    }
}


data class City(
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
