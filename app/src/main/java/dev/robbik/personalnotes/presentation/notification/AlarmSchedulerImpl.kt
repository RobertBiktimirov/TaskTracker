package dev.robbik.personalnotes.presentation.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import dev.robbik.personalnotes.core.notification.AlarmScheduler
import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.core.ui.time.formatWatchs
import dev.robbik.personalnotes.core.ui.time.fullFormat
import dev.robbik.personalnotes.core.ui.time.minuteInMillis
import dev.robbik.personalnotes.receiver.AlarmReceiver
import java.util.Calendar
import javax.inject.Inject

const val TASK_MODEL_INTENT_NAME = "task model"

class AlarmSchedulerImpl @Inject constructor(
    private val context: Context,
) : AlarmScheduler<TaskModel> {

    private val manager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(t: TaskModel, time: Long) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(TASK_MODEL_INTENT_NAME, t)
        }

        val pending = PendingIntent.getBroadcast(
            context,
            t.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        manager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pending
        )
    }

    override fun cancel(t: TaskModel) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingID = t.hashCode()
        val pending = PendingIntent.getBroadcast(
            context,
            pendingID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        manager.cancel(pending)
    }
}