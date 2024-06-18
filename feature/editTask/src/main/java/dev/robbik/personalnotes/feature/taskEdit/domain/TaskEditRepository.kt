package dev.robbik.personalnotes.feature.taskEdit.domain

import dev.robbik.personalnotes.core.task.domain.model.SubtaskModel
import dev.robbik.personalnotes.core.task.domain.model.TaskModel

interface TaskEditRepository {

    suspend fun saveTask(taskModel: TaskModel): Result<Unit>

    suspend fun deleteTask(taskId: Int): Result<Unit>

    suspend fun getTaskById(taskId: Int): Result<TaskModel>

    suspend fun updateSubtask(
        taskId: Int,
        newSubtask: List<SubtaskModel>
    ): Result<List<SubtaskModel>>

    suspend fun updateTask(taskModel: TaskModel): Result<Unit>
}