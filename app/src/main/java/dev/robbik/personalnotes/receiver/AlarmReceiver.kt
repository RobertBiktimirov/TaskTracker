package dev.robbik.personalnotes.receiver

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dev.robbik.personalnotes.R
import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.core.task.utils.constans.TASK_PARAM_ID
import dev.robbik.personalnotes.presentation.MainActivity
import dev.robbik.personalnotes.presentation.notification.TASK_MODEL_INTENT_NAME

const val CHANNEL_ID = "TASK TRACKER NOTIFY"

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val taskModel = intent?.getParcelableExtra<TaskModel>(TASK_MODEL_INTENT_NAME) ?: return
        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val contentIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(TASK_PARAM_ID, taskModel.taskId)
        }

        val notification = Notification.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(taskModel.title)
            .setContentText("Время напоминания о задаче!\n${taskModel.description}")
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    taskModel.hashCode(),
                    contentIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .setAutoCancel(true)
            .build()

        manager.notify(taskModel.hashCode(), notification)
    }
}