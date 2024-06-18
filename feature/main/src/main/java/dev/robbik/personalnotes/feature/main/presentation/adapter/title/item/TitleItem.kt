package dev.robbik.personalnotes.feature.main.presentation.adapter.title.item

import dev.robbik.personalnotes.core.ui.recycler.OutlineItem

data class TitleItem(
    override val content: String,
    override val id: Int = content.hashCode()
) : OutlineItem {

    override fun compareToOther(other: OutlineItem): Boolean {
        return (other as TitleItem).content == content
    }
}