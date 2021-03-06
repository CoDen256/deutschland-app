package de.app.api.applications

import android.content.Context
import de.app.R
import java.time.LocalDateTime

/**
 * Application
 *
 * @property serviceId
 * @property accountId
 * @property name
 * @property description
 * @property status
 * @property applicationDate
 * @constructor Create empty Application
 */
data class Application (
    val serviceId: String,
    val accountId: String,
    val name: String,
    val description: String,
    val status: ApplicationStatus,
    val applicationDate: LocalDateTime
)

/**
 * Application status
 *
 * @property order
 * @constructor Create empty Application status
 */
enum class ApplicationStatus(val order: Int) {
    /**
     * Sent
     *
     * @constructor Create empty Sent
     */
    SENT(0),

    /**
     * Verification
     *
     * @constructor Create empty Verification
     */
    VERIFICATION(1),

    /**
     * Processing
     *
     * @constructor Create empty Processing
     */
    PROCESSING(2),

    /**
     * Done
     *
     * @constructor Create empty Done
     */
    DONE(3),

    /**
     * Rejected
     *
     * @constructor Create empty Rejected
     */
    REJECTED(3);


    fun toString(context: Context): String{
        return context.getString(when(this){
            DONE -> R.string.application_done
            SENT -> R.string.application_sent
            REJECTED -> R.string.application_rejected
            PROCESSING -> R.string.application_processing
            VERIFICATION -> R.string.application_verification
        })
    }
}
