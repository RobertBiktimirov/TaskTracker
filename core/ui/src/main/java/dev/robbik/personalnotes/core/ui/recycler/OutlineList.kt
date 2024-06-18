package dev.robbik.personalnotes.core.ui.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

interface OutlineItem {
    val content: Any
    val id: Int
    fun compareToOther(other: OutlineItem): Boolean
}

interface AdapterOutlineDelegate {

    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: OutlineItem, position: Int)

    fun isOfViewType(item: OutlineItem): Boolean
}

class OutlineAdapterItemCallback : DiffUtil.ItemCallback<OutlineItem>() {
    override fun areItemsTheSame(oldItem: OutlineItem, newItem: OutlineItem): Boolean {
        return oldItem::class == newItem::class && oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: OutlineItem, newItem: OutlineItem): Boolean {
        return oldItem.compareToOther(newItem)
    }
}

class OutlineAdapter : ListAdapter<OutlineItem, RecyclerView.ViewHolder>(OutlineAdapterItemCallback()) {

    private val delegates: MutableList<AdapterOutlineDelegate> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        delegates[viewType].onCreateViewHolder(parent)


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegates[getItemViewType(position)].onBindViewHolder(holder, getItem(position), position)
    }

    fun addDelegates(vararg delegate: AdapterOutlineDelegate) {
        delegate.forEach {
            delegates.add(it)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return delegates.indexOfFirst { it.isOfViewType(currentList[position]) }
    }
}

