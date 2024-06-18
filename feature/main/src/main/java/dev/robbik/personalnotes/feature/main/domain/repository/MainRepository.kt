package dev.robbik.personalnotes.feature.main.domain.repository

import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun getTasksFlow(): Flow<List<TaskModel>>

    suspend fun deleteTask(taskId: Int): Result<Unit>

    suspend fun changeIsCompletedTask(taskId: Int): Result<Unit>
}