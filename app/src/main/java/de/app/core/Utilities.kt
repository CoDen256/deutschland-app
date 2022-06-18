package de.app.core

import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

fun runWithInterval(runnable: () -> Unit, period: Long = 10000) {
    Timer().scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            runnable()
        }
    }, 0, period)
}

fun <T : Any> T.success(): Result<T> = Result.success(this)
fun success(): Result<Unit> = Unit.success()

fun <T : Any> T?.successOrElse(default: T): Result<T> {
    return this?.success() ?: default.success()
}

fun <T : Any> T?.successOrElse(exception: Exception): Result<T> {
    return this?.success() ?: Result.failure(exception)
}

inline fun <reified T : Any> T?.successOrElse(): Result<T> {
    return this?.success()
        ?: Result.failure(IllegalArgumentException("Element was not found ${T::class.simpleName}"))
}

fun range(from: LocalDateTime?, to:LocalDateTime?) =
    (from?: LocalDateTime.MIN)..(to?: LocalDateTime.MAX)

fun range(from: LocalDate?, to:LocalDate?) =
    (from?: LocalDate.MIN)..(to?: LocalDate.MAX)