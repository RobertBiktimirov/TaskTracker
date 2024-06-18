package dev.robbik.personalnotes.core.ui.time

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

val Long.fullFormat: String
    get() {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return format.format(this)
    }

val Long?.formatWatchs: String?
    get() {
        this ?: return null
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(this)
    }

val Long?.day: String?
    get() {
        this ?: return null
        val format = SimpleDateFormat("dd", Locale.getDefault())
        return format.format(this)
    }

val Long?.formatDate: String?
    get() {
        this ?: return null

        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(this)
    }

val Long.plusDay: Long
    get() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return calendar.timeInMillis
    }

val startNowDay: Long
    get() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

val Long.startDay: Long
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = Date(this)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

val Int.minuteInMillis: Long
    get() = this.toLong() * 60 * 1000
