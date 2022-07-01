package de.app.notifications.notificator

import android.content.Context
import de.app.R
import de.app.api.law.LawChange
import de.app.api.law.LawRegistryService
import io.karn.notify.Notify
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LawChangeNotificator @Inject constructor(): BaseNotificator<LawChange>() {

    private var lastFetch: LocalDateTime = LocalDateTime.now()

    @Inject lateinit var service: LawRegistryService

    override fun collect(): List<LawChange> {
        return service.getLawChanges(from=lastFetch, to=LocalDateTime.now()).also {
            lastFetch = LocalDateTime.now()
        }
    }

    override fun notify(context: Context, data: List<LawChange>) {
        Notify.with(context)
            .asBigText {
                title = context.getString(R.string.notify_new_law)
                expandedText = data.first().description
                text = data.first().name
                bigText = data.first().name
            }.show()
    }

}