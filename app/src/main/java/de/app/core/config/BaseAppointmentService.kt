package de.app.core.config

import de.app.api.appointment.Appointment
import de.app.api.appointment.AppointmentService
import de.app.core.config.DataGenerator.Companion.generateAppointments
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseAppointmentService @Inject constructor(): AppointmentService {
    private val appointments = generateAppointments(40, BaseAdministrativeServiceRegistry.services)

    override fun getAllAppointmentsByAccountId(accountId: String): List<Appointment> {
        return appointments.filter { it.accountId == accountId }
    }

    override fun addAppointmentForAccountId(
        accountId: String,
        appointment: Appointment
    ): Result<Unit> {
        TODO("Not yet implemented")
    }
}