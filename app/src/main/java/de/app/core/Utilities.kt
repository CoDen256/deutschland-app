package de.app.core

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.Executors

fun runWithInterval(runnable: () -> Unit, period: Long = 10000) {
    Timer().scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            runnable()
        }
    }, 0, period)
}

fun inSeparateThread(runnable: () -> Unit){
    Executors.newSingleThreadExecutor().execute {
        runnable()
    }
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

fun <T, R> Task<T>.onSuccess(onSuccess: (T) -> R):Task<R>{
    return this.onSuccessTask { Tasks.forResult(onSuccess(it)) }
}

fun <R> mapFromArray(vararg elements: R): Map<Int, R>{
    return mapOf(*elements.asList().mapIndexed { i, e -> i to e }.toTypedArray())
}

inline fun <T, reified C:T> T?.applyIf(block: C.() -> Unit): T? {
    if (this is C){
        block(this)
    }
    return this
}

