package de.app.config

import android.content.Context
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import de.app.core.success
import de.app.ui.util.toast
import java.io.IOException
import java.lang.reflect.Type
import java.time.LocalDate

abstract class AssetDataSource<T, O> {

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateConverter())
        .create()
    private val data: MutableList<T> = ArrayList()

    private fun getJsonDataFromAsset(context: Context, fileName: String): Result<String> {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }.success()
        } catch (ioException: IOException) {
            Result.failure(ioException)
        }
    }

    fun init(context: Context, fileName: String) {
        getJsonDataFromAsset(context, fileName).mapCatching {
            val type = object : TypeToken<List<LawChangeAsset>>() {}.type
            val elements = gson.fromJson<List<T>>(it, type)
            data.addAll(elements)
        }.onFailure {
            context.toast("Unable to fetch data for ${this.javaClass.simpleName}: ${it.message}")
        }
    }

//    abstract fun map()

    fun getAll(): List<T>{
        return data
    }
}

class LocalDateConverter :JsonDeserializer<LocalDate> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext
    ): LocalDate = LocalDate.parse(json.asJsonPrimitive.asString)
}