package de.app.api.appointment

interface AppoinmentService {

    fun getAllAppointmentsByAccountId(accountId: String): Result<List<Appointment>>

    // THIS IS USED BY Service Provider, not the application
    fun addAppointmentForAccountId(accountId: String, appointment: Appointment): Result<Unit>
}


