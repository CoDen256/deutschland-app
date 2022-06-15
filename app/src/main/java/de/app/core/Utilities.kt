package de.app.core

import java.util.*

fun runWithInterval(runnable: () -> Unit, period: Long = 10000) {
    Timer().scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            runnable()
        }
    }, 0, period)
}