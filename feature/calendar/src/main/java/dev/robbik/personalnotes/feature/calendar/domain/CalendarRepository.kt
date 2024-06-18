package dev.robbik.personalnotes.feature.calendar.domain

import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {

    suspend fun getTaskByDay(startTime: Long): Result<List<TaskModel>>

    fun getTaskByDayFlow(startTime: Long): Flow<List<TaskModel>>

    suspend fun deleteTasks(taskId: Int): Result<Unit>

    suspend fun changeIsCompletedTask(taskId: Int): Result<Unit>
}