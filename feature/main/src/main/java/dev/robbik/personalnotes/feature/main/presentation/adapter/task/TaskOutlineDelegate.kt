package dev.robbik.personalnotes.feature.main.presentation.adapter.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import dev.robbik.personalnotes.core.task.databinding.ItemTaskBinding
import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.core.ui.persent.percent
import dev.robbik.personalnotes.core.ui.recycler.AdapterOutlineDelegate
import dev.robbik.personalnotes.core.ui.recycler.OutlineItem
import dev.robbik.personalnotes.core.ui.time.formatWatchs
import dev.robbik.personalnotes.feature.main.presentation.adapter.task.item.TaskItem

class TaskOutlineDelegate(
    private val taskOnClickListener: (taskId: Int) -> Unit,
    private val deleteTaskClickListener: (taskId: Int) -> Unit,
) : AdapterOutlineDelegate {

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(taskItem: TaskModel) = with(binding) {

            task.setOnClickListener {
                taskOnClickListener(taskItem.taskId)
            }

            task.setOnLongClickListener {
                editTaskContainer.isVisible = !editTaskContainer.isVisible
                true
            }

            deleteTask.setOnClickListener {
                deleteTaskClickListener(taskItem.taskId)
            }

            closeEditTask.setOnClickListener {
                editTaskContainer.isVisible = false
            }

            taskTitle.text = taskItem.title
            taskDescription.text = taskItem.description

            deadline.text = taskItem.deadline?.formatWatchs ?: ""

            subtasks.isVisible = taskItem.isHaveSubtasks
            progress.isVisible = taskItem.isHaveSubtasks
            progressValue.isVisible = taskItem.isHaveSubtasks

            subtasks.text = taskItem.subTasksString

            taskItem.progress?.let {
                progress.progress = it
            }

            progressValue.text = taskItem.progress.percent
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(
            inflater,
            parent,
            false
        )
        val viewHolder = TaskViewHolder(binding)

        return viewHolder
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: OutlineItem,
        position: Int
    ) {
        (holder as TaskViewHolder).bind(item.content as TaskModel)
    }

    override fun isOfViewType(item: OutlineItem): Boolean = item is TaskItem
}