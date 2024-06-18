package dev.robbik.personalnotes.feature.calendar.data

import android.util.Log
import dev.robbik.personalnotes.core.database.TaskDao
import dev.robbik.personalnotes.core.task.domain.model.SubtaskModel
import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.core.ui.time.day
import dev.robbik.personalnotes.core.ui.time.formatWatchs
import dev.robbik.personalnotes.core.ui.time.plusDay
import dev.robbik.personalnotes.feature.calendar.domain.CalendarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : CalendarRepository {

    override suspend fun getTaskByDay(startTime: Long): Result<List<TaskModel>> =
        withContext(Dispatchers.IO) {
            return@withContext kotlin.runCatching {
                taskDao.getTaskInTimeRange(startTime, startTime.plusDay).map { taskEntity ->
                    TaskModel(
                        taskEntity.taskId,
                        taskEntity.title,
                        taskEntity.description,
                        taskEntity.deadline,
                        taskEntity.subtasksId.map { SubtaskModel(it.title, it.isCompleted) },
                        taskEntity.isCompleted
                    )
                }
            }
        }

    override fun getTaskByDayFlow(startTime: Long): Flow<List<TaskModel>> {
        return taskDao.getTaskList().map { taskEntityList ->
            taskEntityList
                .filter { it.deadline != null && it.deadline!! >= startTime && it.deadline!! < startTime.plusDay }
                .map { taskEntity ->
                    TaskModel(
                        taskEntity.taskId,
                        taskEntity.title,
                        taskEntity.description,
                        taskEntity.deadline,
                        taskEntity.subtasksId.map { SubtaskModel(it.title, it.isCompleted) },
                        taskEntity.isCompleted
                    )
                }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteTasks(taskId: Int): Result<Unit> = kotlin.runCatching {
        withContext(Dispatchers.IO) {
            taskDao.deleteTask(taskId)
        }
    }

    override suspend fun changeIsCompletedTask(taskId: Int): Result<Unit> = kotlin.runCatching {
        withContext(Dispatchers.IO) {
            taskDao.changeIsCompletedTask(taskId)
        }
    }
}