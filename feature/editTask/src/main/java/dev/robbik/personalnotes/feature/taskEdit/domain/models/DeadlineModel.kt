package dev.robbik.personalnotes.feature.taskEdit.domain.models

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DeadlineModel(
    val time: TimeModel?,
    var day: Long
) {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val currentDateInMillis: Long
        get() {

            val calendar = Calendar.getInstance()

            calendar.time = Date(day)
            calendar.set(Calendar.HOUR_OF_DAY, time?.hour ?: calendar.get(Calendar.HOUR_OF_DAY))
            calendar.set(Calendar.MINUTE, time?.minute ?: calendar.get(Calendar.MINUTE))

            return calendar.timeInMillis
        }


    val formatTime: String
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = Date(day)

            val hour: Int
            val minutes: Int

            if (time == null) {
                hour = calendar.get(Calendar.HOUR_OF_DAY)
                minutes = calendar.get(Calendar.MINUTE)
            } else {
                hour = time.hour
                minutes = time.minute
            }
            return "$hour : $minutes"
        }

    val formatDate: String
        get() = dateFormat.format(day)


    companion object {

        val Init: DeadlineModel
            get() {
                val nowTime = Calendar.getInstance()
                val timeModel = TimeModel(nowTime.get(Calendar.HOUR_OF_DAY), nowTime.get(Calendar.MINUTE))
                return DeadlineModel(timeModel, nowTime.timeInMillis)
            }

    }

}

data class TimeModel(
    val hour: Int,
    val minute: Int
)