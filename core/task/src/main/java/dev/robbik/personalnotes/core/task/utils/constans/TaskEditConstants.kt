package dev.robbik.personalnotes.core.task.utils.constans

enum class TaskEditConstants(val value: String) {

    UPDATE_REQUEST("update request"),

    /** Для получения результата обновления подзадач */
    UPDATE_SUBTASK_KEY("update subtask key"),
}

const val TASK_PARAM_ID = "task id"
const val DEFAULT_TASK_ID_PARAM = -1