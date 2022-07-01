package de.app.notifications.notificator

import android.content.Context
import de.app.api.law.LawChangeHeader
import de.app.api.law.LawRegistryService
import io.karn.notify.Notify
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LawChangeNotificator @Inject constructor(): BaseNotificator<LawChangeHeader>() {

    private var lastFetch: LocalDate = LocalDate.now().minusDays(15)

    @Inject lateinit var service: LawRegistryService

    override fun collect(): List<LawChangeHeader> {
        return service.getLawChanges(lastFetch).also {
            lastFetch = LocalDate.now()
        }
    }

    override fun notify(context: Context, data: List<LawChangeHeader>) {
        Notify.with(context)
            .asBigText {
                title = "A new law has been registered"
                expandedText = data.first().shortDescription
                text = data.first().name
                bigText = data.first().name
            }.show()
    }

}