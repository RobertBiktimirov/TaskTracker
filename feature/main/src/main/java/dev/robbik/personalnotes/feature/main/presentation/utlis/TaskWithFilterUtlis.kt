package dev.robbik.personalnotes.feature.main.presentation.utlis

import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.core.ui.time.plusDay
import dev.robbik.personalnotes.core.ui.time.startNowDay
import dev.robbik.personalnotes.feature.main.presentation.filter.FilterModel
import dev.robbik.personalnotes.feature.main.presentation.filter.FilterType.*

fun List<TaskModel>.withFilterTask(filter: FilterModel): List<TaskModel> {
    return when (filter.type) {
        ALL -> this
        TODAY -> {
            filter {
                val deadline = it.deadline
                deadline != null && deadline >= startNowDay && deadline < startNowDay.plusDay
            }
        }

        OPEN -> filter { it.isCompleted.not() }
        COMPLETED -> filter { it.isCompleted }
//        WITH_DEADLINE -> filter { it.deadline != null }
//        NOT_WITH_DEADLINE -> filter { it.deadline == null }
        WITH_SUBTASK -> filter { it.subtasks.isNotEmpty() }
        NOT_WITH_SUBTASK -> filter { it.subtasks.isEmpty() }
    }
}