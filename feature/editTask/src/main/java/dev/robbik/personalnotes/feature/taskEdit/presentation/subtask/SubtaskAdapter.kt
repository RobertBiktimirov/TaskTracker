package dev.robbik.personalnotes.feature.taskEdit.presentation.subtask

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import dev.robbik.personalnotes.core.task.domain.model.SubtaskModel
import dev.robbik.personalnotes.feature.taskEdit.R
import dev.robbik.personalnotes.feature.taskEdit.databinding.AddSubtasksItemBinding
import dev.robbik.personalnotes.feature.taskEdit.databinding.SubtasksItemBinding

class SubtaskAdapter(
    private val subTaskViewType: SubtaskViewType = SubtaskViewType.PREVIEW,
    private val closeClickListener: (task: SubtaskModel) -> Int? = { null },
    private val doAfterTextChanged: (text: String, task: SubtaskModel) -> Unit = { _, _ -> },
    private val isCompletedClickListener: (subtask: SubtaskModel) -> Boolean = { false }
) : ListAdapter<SubtaskModel, SubtaskAdapter.SubtasksViewHolder>(SubtaskDiffUtil()) {

    sealed class SubtasksViewHolder(
        binding: ViewBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(subtask: SubtaskModel)
        open fun changeIsSelectedState(newIsCompleted: Boolean) {}

        class PreviewSubtaskViewHolder(
            private val binding: SubtasksItemBinding,
        ) : SubtasksViewHolder(binding) {

            override fun bind(subtask: SubtaskModel) {
                binding.closeIcon.isVisible = false
                binding.nameTask.apply {
                    setText(subtask.name)
                    isClickable = false
                    isFocusable = false
                    isLongClickable = false
                }

                binding.isCompletetTask.apply {
                    setImageResource(
                        if (subtask.isCompleted) R.drawable.baseline_done_24 else 0
                    )

                    isVisible = true
                }
            }

            override fun changeIsSelectedState(newIsCompleted: Boolean) {
                binding.isCompletetTask.setImageResource(
                    if (newIsCompleted) R.drawable.baseline_done_24 else 0
                )
            }
        }

        class AddSubtaskViewHolder(
            private val binding: AddSubtasksItemBinding,
        ) : SubtasksViewHolder(binding) {
            override fun bind(subtask: SubtaskModel) {
                binding.nameTask.setText(subtask.name)
            }
        }

        class EditSubtaskViewHolder(
            private val binding: SubtasksItemBinding,
        ) : SubtasksViewHolder(binding) {
            override fun bind(subtask: SubtaskModel) {
                binding.nameTask.setText(subtask.name)

                binding.isCompletetTask.apply {
                    setImageResource(
                        if (subtask.isCompleted) R.drawable.baseline_done_24 else 0
                    )
                    isVisible = true
                }
            }

            override fun changeIsSelectedState(newIsCompleted: Boolean) {
                binding.isCompletetTask.setImageResource(
                    if (newIsCompleted) R.drawable.baseline_done_24 else 0
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtasksViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val subtaskItemBinding = SubtasksItemBinding.inflate(inflater, parent, false)
        val addSubtaskBinding = AddSubtasksItemBinding.inflate(inflater, parent, false)

        val holder = when (subTaskViewType) {
            SubtaskViewType.ADD -> SubtasksViewHolder.AddSubtaskViewHolder(addSubtaskBinding)
            SubtaskViewType.PREVIEW -> SubtasksViewHolder.PreviewSubtaskViewHolder(subtaskItemBinding)
            SubtaskViewType.EDIT -> SubtasksViewHolder.EditSubtaskViewHolder(subtaskItemBinding)
        }

        subtaskItemBinding.closeIcon.setOnClickListener {
            val position = closeClickListener(getItem(holder.absoluteAdapterPosition))
                ?: return@setOnClickListener

            if (subTaskViewType == SubtaskViewType.ADD) notifyItemRemoved(position)
        }

        subtaskItemBinding.nameTask.doAfterTextChanged {
            doAfterTextChanged(it.toString(), getItem(holder.absoluteAdapterPosition))
        }

        addSubtaskBinding.nameTask.doAfterTextChanged {
            doAfterTextChanged(it.toString(), getItem(holder.absoluteAdapterPosition))
        }

        addSubtaskBinding.closeIcon.setOnClickListener {
            val position = closeClickListener(getItem(holder.absoluteAdapterPosition))
                ?: return@setOnClickListener

            if (position == 0) addSubtaskBinding.nameTask.clearFocus()
            notifyItemRemoved(position)
        }

        subtaskItemBinding.isCompletetTask.setOnClickListener {
            val newBoolean = isCompletedClickListener(getItem(holder.absoluteAdapterPosition))
            holder.changeIsSelectedState(newBoolean)
        }

        return holder
    }

    override fun onBindViewHolder(holder: SubtasksViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class SubtaskDiffUtil : ItemCallback<SubtaskModel>() {
    override fun areItemsTheSame(oldItem: SubtaskModel, newItem: SubtaskModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SubtaskModel, newItem: SubtaskModel): Boolean {
        return oldItem == newItem
    }
}