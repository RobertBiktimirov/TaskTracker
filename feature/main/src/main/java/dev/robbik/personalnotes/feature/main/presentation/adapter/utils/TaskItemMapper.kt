package dev.robbik.personalnotes.feature.main.presentation.adapter.utils

import android.annotation.SuppressLint
import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.core.ui.recycler.OutlineItem
import dev.robbik.personalnotes.core.ui.time.plusDay
import dev.robbik.personalnotes.core.ui.time.startNowDay
import dev.robbik.personalnotes.feature.main.presentation.adapter.task.item.TaskItem
import dev.robbik.personalnotes.feature.main.presentation.adapter.title.item.TitleItem
import kotlin.random.Random

@SuppressLint("BuildListAdds")
fun List<TaskModel>.toTasksWithTime(): List<OutlineItem> {

    if (this.isEmpty()) return emptyList()

    val outlinesList = mutableListOf<OutlineItem>()

    outlinesList.add(TitleItem("В прошлом"))
    var index = 0
    while (index < this.size && (this[index].deadline ?: 0) < startNowDay) {
        outlinesList.add(TaskItem(Random.nextInt(), this[index]))
        index++
    }

    if (index == size) return outlinesList

    outlinesList.add(TitleItem("Сегодня"))

    while (index < this.size && (this[index].deadline ?: 0) < startNowDay.plusDay) {
        outlinesList.add(TaskItem(Random.nextInt(), this[index]))
        index++
    }

    if (index == size) return outlinesList

    outlinesList.add(TitleItem("В будущем"))

    while (index < this.size) {
        outlinesList.add(TaskItem(Random.nextInt(), this[index]))
        index++
    }

    return outlinesList
}