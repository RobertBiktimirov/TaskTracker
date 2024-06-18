package dev.robbik.personalnotes.core.notification

interface AlarmScheduler<T> {
    fun schedule(t: T, time: Long)

    fun cancel(t: T)
}