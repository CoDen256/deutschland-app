package de.app.config

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import de.app.core.success
import de.app.ui.util.toast
import java.io.IOException
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime

abstract class AssetDataSource<T, O>(
    private val context: Context,
    private val fileName: String) {

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateConverter())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeConverter())
        .create()

    val data: List<T> by lazy {
        initialFetch(context, fileName)
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): Result<String> {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }.success()
        } catch (ioException: IOException) {
            Result.failure(ioException)
        }
    }

    private fun initialFetch(context: Context, fileName: String): List<T> {
        return getJsonDataFromAsset(context, fileName).mapCatching {
            gson.fromJson<List<O>>(it, getJsonType())
        }.mapCatching { list ->
            mapList(list)
        }.getOrThrow()
    }

    private fun mapList(list: List<O>) = list.map { map(it) }

    abstract fun map(origin: O) : T
    abstract fun getJsonType(): Type
}

class LocalDateConverter :JsonDeserializer<LocalDate> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext
    ): LocalDate = LocalDate.parse(json.asJsonPrimitive.asString)
}

class LocalDateTimeConverter :JsonDeserializer<LocalDateTime> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext
    ): LocalDateTime = LocalDateTime.parse(json.asJsonPrimitive.asString)
}