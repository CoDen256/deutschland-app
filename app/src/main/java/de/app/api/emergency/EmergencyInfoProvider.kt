package de.app.api.emergency

import java.time.LocalDateTime

interface EmergencyInfoProvider {
    fun getAllEmergenciesForCity(city: String,
                                 from: LocalDateTime? = null,
                                 to: LocalDateTime? = null): List<Emergency>
    fun getAllEmergenciesForCountry(country: String,
                                    from: LocalDateTime? = null,
                                    to: LocalDateTime? = null): List<Emergency>
}