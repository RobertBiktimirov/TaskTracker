package dev.robbik.personalnotes.feature.main.presentation.adapter.task.item

import dev.robbik.personalnotes.core.ui.recycler.OutlineItem
import dev.robbik.personalnotes.core.task.domain.model.TaskModel


data class TaskItem(
    val taskId: Int,
    val taskModel: TaskModel
) : OutlineItem {

    override val content: Any get() = taskModel
    override val id: Int get() = taskId

    override fun compareToOther(other: OutlineItem): Boolean {
        return (other as TaskItem).taskModel == taskModel
    }
}