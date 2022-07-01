package de.app.config

import de.app.api.emergency.Emergency
import de.app.api.emergency.EmergencyInfoProvider
import de.app.config.DataGenerator.Companion.generateEmergencies
import de.app.core.range
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseEmergencyInfoProvider @Inject constructor(): EmergencyInfoProvider {
    private val emergencies: List<Emergency> = generateEmergencies(20)

    override fun getAllEmergenciesForCity(
        city: String,
        from: LocalDateTime?,
        to: LocalDateTime?
    ): List<Emergency> {
        return emergencies
            .filter {it.date in range(from, to)}
            .filter { city == it.city }
    }

    override fun getAllEmergenciesForCountry(
        country: String,
        from: LocalDateTime?,
        to: LocalDateTime?
    ): List<Emergency> {
        return emergencies
            .filter{ it.date in range(from, to)}
            .filter { country == it.city }
    }
}