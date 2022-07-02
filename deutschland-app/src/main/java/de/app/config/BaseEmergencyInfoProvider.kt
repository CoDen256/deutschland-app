package de.app.config

import android.content.Context
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import de.app.api.emergency.Emergency
import de.app.api.emergency.EmergencyInfoProvider
import de.app.api.emergency.EmergencySeverity
import de.app.core.range
import java.lang.reflect.Type
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseEmergencyInfoProvider @Inject constructor(
    private val dataSource: EmergencyAssetDataSource
) : EmergencyInfoProvider {

    private val secondDelay = 10L
    private val emergencies: MutableList<Emergency> by lazy {
        fakeFirstEmergencyAsNew(dataSource.data)
    }

    override fun getAllEmergenciesForCity(
        city: String,
        from: LocalDateTime?,
        to: LocalDateTime?
    ): List<Emergency> {
        return emergencies
            .filter { it.dateTime in range(from, to) }
            .filter { city == it.city }
    }

    override fun getAllEmergenciesForCountry(
        country: String,
        from: LocalDateTime?,
        to: LocalDateTime?
    ): List<Emergency> {
        return emergencies
            .filter { it.dateTime in range(from, to) }
            .filter { country == it.city }
    }

    private fun fakeFirstEmergencyAsNew(changes: List<Emergency>): MutableList<Emergency> =
        ArrayList(changes).apply {
            replaceAll {
                if (it.id == 1) it.copy(dateTime = LocalDateTime.now().plusSeconds(secondDelay))
                else it
            }
        }
}

@Singleton
class EmergencyAssetDataSource @Inject constructor(@ApplicationContext private val context: Context) :
    AssetDataSource<Emergency, EmergencyAsset>(context, "common/emergencies.json") {
    override fun map(origin: EmergencyAsset): Emergency {
        return Emergency(
            id = origin.id,
            name = origin.name,
            description = origin.description,
            city = origin.city,
            postalCode = origin.postalCode,
            country = origin.country,
            dateTime = origin.`date-time`,
            severity = EmergencySeverity.valueOf(origin.severity),
        )
    }

    override fun getJsonType(): Type = object : TypeToken<List<EmergencyAsset>>() {}.type
}

data class EmergencyAsset(
    val postalCode: String,
    val country: String,
    val city: String,
    val `date-time`: LocalDateTime,
    val name: String,
    val description: String,
    val id: Int,
    val severity: String,
)