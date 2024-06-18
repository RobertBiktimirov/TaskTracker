package dev.robbik.personalnotes.core.task.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.robbik.personalnotes.core.task.R
import dev.robbik.personalnotes.core.task.databinding.ItemTaskBinding
import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.core.ui.persent.percent
import dev.robbik.personalnotes.core.ui.time.formatWatchs
import dev.robbik.personalnotes.core.ui.time.fullFormat

class TaskAdapter(
    private val taskOnClickListener: (taskId: Int) -> Unit,
    private val deleteTaskClickListener: (taskId: Int) -> Unit,
    private val changeIsCompleteTaskClickListener: (taskId: Int) -> Unit
) : ListAdapter<TaskModel, TaskAdapter.TaskViewHolder>(
    TaskDiffUtil()
) {

    inner class TaskViewHolder(private val binding: ItemTaskBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(taskItem: TaskModel) = with(binding) {

            taskTitle.text = taskItem.title
            taskDescription.text = taskItem.description

            deadline.text = taskItem.deadline?.fullFormat ?: ""

            subtasks.isVisible = taskItem.isHaveSubtasks
            progress.isVisible = taskItem.isHaveSubtasks
            progressValue.isVisible = taskItem.isHaveSubtasks

            subtasks.text = taskItem.subTasksString

            taskItem.progress?.let {
                progress.progress = it
            }

            progressValue.text = taskItem.progress.percent

            binding.task.alpha = if (taskItem.isCompleted) 0.6f else 1f

            binding.changeCompleteTask.text =
                if (taskItem.isCompleted) context.getString(R.string.core_task_open_task_title)
                else context.getString(R.string.core_task_complete_task_title)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(
            inflater,
            parent,
            false
        )
        val viewHolder = TaskViewHolder(binding, parent.context)

        with(binding) {
            task.setOnClickListener {
                taskOnClickListener(getItem(viewHolder.absoluteAdapterPosition).taskId)
            }

            task.setOnLongClickListener {
                editTaskContainer.isVisible = !editTaskContainer.isVisible
                true
            }

            deleteTask.setOnClickListener {
                deleteTaskClickListener(getItem(viewHolder.absoluteAdapterPosition).taskId)
            }

            closeEditTask.setOnClickListener {
                editTaskContainer.isVisible = false
            }

            changeCompleteTask.setOnClickListener {
                changeIsCompleteTaskClickListener(getItem(viewHolder.absoluteAdapterPosition).taskId)
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class TaskDiffUtil : DiffUtil.ItemCallback<TaskModel>() {
    override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
        oldItem.taskId == newItem.taskId

    override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
        oldItem == newItem

}