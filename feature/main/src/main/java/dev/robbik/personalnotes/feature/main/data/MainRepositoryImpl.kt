package dev.robbik.personalnotes.feature.main.data

import dev.robbik.personalnotes.core.database.TaskDao
import dev.robbik.personalnotes.core.task.domain.model.SubtaskModel
import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.feature.main.domain.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : MainRepository {

    override fun getTasksFlow(): Flow<List<TaskModel>> {
        return taskDao.getTaskList().map { list ->
            list.map { taskEntity ->
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

    override suspend fun deleteTask(taskId: Int): Result<Unit> = kotlin.runCatching {
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