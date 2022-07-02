package de.app.config

import de.app.api.appointment.Appointment
import de.app.api.appointment.AppointmentService
import de.app.config.DataGenerator.Companion.generateAppointments
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseAppointmentService @Inject constructor(): AppointmentService {
    private val appointments = ArrayList(
        generateAppointments(40, BaseAdministrativeServiceRegistry.services)
    )

    override fun getAllAppointmentsByAccountId(accountId: String): List<Appointment> {
        return appointments.filter { it.accountId == accountId }
    }

    override fun addAppointmentForAccountId(
        accountId: String,
        appointment: Appointment
    ): Result<Unit> {
        appointments.add(appointment)
        return Result.success(Unit)
    }
}