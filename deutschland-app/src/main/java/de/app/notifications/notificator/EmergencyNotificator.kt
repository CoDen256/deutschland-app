package de.app.notifications.notificator

import android.content.Context
import de.app.R
import de.app.api.emergency.Emergency
import de.app.api.emergency.EmergencyInfoProvider
import de.app.api.emergency.EmergencySeverity
import de.app.core.SessionManager
import de.app.core.inSeparateThread
import io.karn.notify.Notify
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmergencyNotificator @Inject constructor(): Notificator {

    private var lastFetch: LocalDateTime = LocalDateTime.now()

    @Inject lateinit var service: EmergencyInfoProvider
    @Inject lateinit var manager: SessionManager

    override fun trigger(context: Context) {
        inSeparateThread {
            val emergencies = ArrayList<Emergency>()
            runBlocking {
                manager.getUsers().forEach { user ->
                    emergenciesForCity(user.address.city).firstOrNull()?.let {
                        emergencies.add(it)
                    }
                }
            }
            lastFetch = LocalDateTime.now()
            notify(context, emergencies)
        }
    }


    private fun notify(context: Context, data: List<Emergency>) {
        data.forEach {
            Notify.with(context)
                .header {
                    icon = R.drawable.ic_emergency
                }
                .asBigText {
                    title = context.getString(getTitle(it.severity), it.name)
                    text = it.description
                    expandedText = it.description
                    bigText = it.city + " " + it.postalCode
                }.show()
        }

    }

    private fun emergenciesForCity(city: String) =
        service.getAllEmergenciesForCity(
            city,
            from = lastFetch,
            to = LocalDateTime.now()
        )


    private fun getTitle(severity: EmergencySeverity): Int=
        when (severity) {
            EmergencySeverity.MEDIUM -> R.string.notify_emergency_medium
            EmergencySeverity.HIGH -> R.string.notify_emergency_high
            EmergencySeverity.LOW -> R.string.notify_emergency_low
        }



}