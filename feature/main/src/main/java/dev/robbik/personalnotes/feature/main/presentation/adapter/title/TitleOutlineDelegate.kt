//package dev.robbik.personalnotes.feature.main.presentation.adapter.title
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.Adapter
//import androidx.recyclerview.widget.RecyclerView
//import androidx.recyclerview.widget.RecyclerView.ViewHolder
//import dev.robbik.personalnotes.core.ui.recycler.AdapterOutlineDelegate
//import dev.robbik.personalnotes.core.ui.recycler.OutlineItem
//import dev.robbik.personalnotes.feature.main.presentation.adapter.title.item.TitleItem
//
//class TitleOutlineDelegate : AdapterOutlineDelegate {
//
//    private class TitleViewHolder(private val binding: ItemTitleBinding) :
//        ViewHolder(binding.root) {
//        fun bind(title: String) {
//            binding.title.text = title
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
//        TitleViewHolder(
//            ItemTitleBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//
//    override fun onBindViewHolder(
//        holder: ViewHolder,
//        item: OutlineItem,
//        position: Int
//    ) {
//        (holder as TitleViewHolder).bind(item.content as String)
//    }
//
//    override fun isOfViewType(item: OutlineItem): Boolean = item is TitleItem
//}