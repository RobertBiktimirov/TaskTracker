package dev.robbik.personalnotes.core.task.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskModel(
    val taskId: Int,
    val title: String,
    val description: String,
    val deadline: Long?,
    var subtasks: List<SubtaskModel>,
    val isCompleted: Boolean,
) : Parcelable {

    val isHaveSubtasks: Boolean
        get() = subtasks.isNotEmpty()

    val subTasksString: String
        get() = "Подзадачи: ${subtasks.joinToString(separator = ", ") { it.name }}"


    val progress: Int?
        get() {
            if (subtasks.isEmpty()) return null

            val percent =
                subtasks.filter { it.isCompleted }.size.toFloat() / subtasks.size.toFloat() * 100
            return percent.toInt()
        }
}

@Parcelize
data class SubtaskModel(
    var name: String,
    val isCompleted: Boolean
) : Parcelable
