package de.app.core

import java.lang.IllegalArgumentException
import java.util.*

fun runWithInterval(runnable: () -> Unit, period: Long = 10000) {
    Timer().scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            runnable()
        }
    }, 0, period)
}

fun <T : Any> T.success(): Result<T> = Result.success(this)
fun <T : Any> T?.successOr(default: T): Result<T> {
    return this?.success() ?: default.success()
}

fun Result.Companion.success(): Result<Unit> = success(Unit)

fun <T : Any> T?.successOrThrow(exception: Exception): Result<T> {
    return this?.success() ?: Result.failure(exception)
}

inline fun <reified T : Any> T?.successOrThrow(): Result<T> {
    return this?.success()
        ?: Result.failure(IllegalArgumentException("Element was not found ${T::class.simpleName}"))
}