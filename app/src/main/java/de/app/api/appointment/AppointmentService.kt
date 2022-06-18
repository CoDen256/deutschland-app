package de.app.api.appointment

interface AppointmentService {

    fun getAllAppointmentsByAccountId(accountId: String): List<Appointment>

    // THIS IS USED BY Service Provider, not the application
    fun addAppointmentForAccountId(accountId: String, appointment: Appointment): Result<Unit>
}


