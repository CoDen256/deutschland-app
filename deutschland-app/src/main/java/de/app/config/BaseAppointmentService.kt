package de.app.config

import android.content.Context
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import de.app.api.appointment.Appointment
import de.app.api.appointment.AppointmentService
import de.app.config.common.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseAppointmentService @Inject constructor(
    appointments: AppointmentByAccountDataSource

): AppointmentService {
    private val appointments  by lazy {
        ArrayList(appointments.data.flatten())
    }

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

@Singleton
class AppointmentByAccountDataSource @Inject constructor(
    @ApplicationContext context: Context,
    appointments: AppointmentDataSource,
    addresses: AddressDataSource
) : AssetDataSource<List<Appointment>, AppointmentByAccount>(context, "binding/appointment-by-account.json") {

    private val appointmentById: Map<Int, AppointmentAsset> = appointments.data.associateBy { it.appointmentId }
    private val addressById: Map<Int, AddressAsset> = addresses.data.associateBy { it.id }

    override fun map(origin: AppointmentByAccount): List<Appointment> {
        return origin.appointments.map {
            val app = appointmentById[it.appointmentId]!!
            Appointment(
                serviceId = app.serviceId,accountId= origin.accountId,
                name=app.name, description = app.description,
                appointment = it.dateTime,
                additionalInfo = app.additionalInfo,
                address = addressById[it.addressId]!!.map()
            )
        }
    }

    override fun getJsonType(): Type = object : TypeToken<List<AppointmentByAccount>>() {}.type
}

data class AppointmentByAccount(
    val accountId: String,
    val appointments: List<AppointmentForAccount>
)

data class AppointmentForAccount(
    val appointmentId: Int,
    val dateTime: LocalDateTime,
    val addressId: Int
)