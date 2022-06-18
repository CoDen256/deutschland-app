package de.app.api.emergency

interface EmergencyInfoProvider {
    fun getAllEmergenciesForCity(city: String): Result<List<Emergency>>
    fun getAllEmergenciesForCountry(country: String): Result<List<Emergency>>
}