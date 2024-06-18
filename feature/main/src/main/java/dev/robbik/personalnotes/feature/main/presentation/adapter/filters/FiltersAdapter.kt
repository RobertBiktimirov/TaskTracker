package dev.robbik.personalnotes.feature.main.presentation.adapter.filters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.robbik.personalnotes.core.ui.R as CoreUiR
import dev.robbik.personalnotes.feature.main.databinding.ItemFiltersBinding
import dev.robbik.personalnotes.feature.main.presentation.filter.FilterModel

internal class FiltersAdapter(
    private val onClickListener: (filter: FilterModel) -> Unit
) : ListAdapter<FilterModel, FiltersAdapter.CalendarViewHolder>(CalendarDiffUtil()) {

    internal inner class CalendarViewHolder(
        private val binding: ItemFiltersBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(filter: FilterModel) {
            binding.title.text = filter.type.value

            if (filter.isSelected) binding.setSelectedBackground()
            else binding.setNotSelectedBackground()
        }
    }

    private fun ItemFiltersBinding.setNotSelectedBackground() {
        filterCard.backgroundTintList =
            ColorStateList.valueOf(root.context.getColor(CoreUiR.color.background_task_item))
        title.setTextColor(root.context.getColor(CoreUiR.color.grey_text))
    }

    private fun ItemFiltersBinding.setSelectedBackground() {
        filterCard.backgroundTintList =
            ColorStateList.valueOf(root.context.getColor(CoreUiR.color.primary_color))
        title.setTextColor(root.context.getColor(CoreUiR.color.text_color))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFiltersBinding.inflate(inflater, parent, false)
        val holder = CalendarViewHolder(binding)

        binding.root.setOnClickListener {
            onClickListener(getItem(holder.absoluteAdapterPosition))
        }

        return holder
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) =
        holder.bind(getItem(position))
}

private class CalendarDiffUtil : DiffUtil.ItemCallback<FilterModel>() {
    override fun areItemsTheSame(oldItem: FilterModel, newItem: FilterModel): Boolean {
        return oldItem.type == newItem.type
    }

    override fun areContentsTheSame(oldItem: FilterModel, newItem: FilterModel): Boolean {
        return oldItem == newItem
    }
}