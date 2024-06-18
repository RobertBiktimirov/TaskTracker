package dev.robbik.personalnotes.feature.main.domain.models

data class TaskCounterModel(
    val allTasksCount: Int? = null,
    val doneTasksCount: Int? = null
) {

    val allTasksCountUi: String get() = allTasksCount?.toString() ?: ""
    val doneTasksCountUi: String get() = doneTasksCount?.toString() ?: ""
}