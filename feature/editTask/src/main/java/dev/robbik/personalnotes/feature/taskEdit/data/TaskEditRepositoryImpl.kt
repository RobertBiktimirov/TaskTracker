package dev.robbik.personalnotes.feature.taskEdit.data

import dev.robbik.personalnotes.core.database.TaskDao
import dev.robbik.personalnotes.core.database.entity.SubtaskEntity
import dev.robbik.personalnotes.core.database.entity.TaskEntity
import dev.robbik.personalnotes.core.notification.AlarmScheduler
import dev.robbik.personalnotes.core.task.domain.model.SubtaskModel
import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.feature.taskEdit.domain.TaskEditRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class TaskEditRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val alarmScheduler: AlarmScheduler<TaskModel>
) : TaskEditRepository {

    override suspend fun saveTask(taskModel: TaskModel): Result<Unit> =
        withContext(Dispatchers.IO) {
            return@withContext runCatching {
                taskDao.saveTask(
                    TaskEntity(
                        title = taskModel.title,
                        description = taskModel.description,
                        deadline = taskModel.deadline,
                        subtasksId = taskModel.subtasks.map {
                            SubtaskEntity(title = it.name, it.isCompleted)
                        },
                        isCompleted = taskModel.isCompleted
                    )
                )

                taskDao.getLastNewTask().also { entity ->
                    entity.deadline?.let {
                        alarmScheduler.schedule(taskModel.copy(taskId = entity.taskId), it)
                    }
                }

                return@runCatching
            }
        }

    override suspend fun deleteTask(taskId: Int): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext kotlin.runCatching {
            taskDao.deleteTask(taskId)
        }
    }

    override suspend fun getTaskById(taskId: Int): Result<TaskModel> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val entity = taskDao.getTaskById(taskId)
            TaskModel(
                taskId = entity.taskId,
                title = entity.title,
                description = entity.description,
                deadline = entity.deadline,
                subtasks = entity.subtasksId.map { SubtaskModel(it.title, it.isCompleted) },
                isCompleted = entity.isCompleted
            )
        }
    }

    override suspend fun updateSubtask(
        taskId: Int,
        newSubtask: List<SubtaskModel>
    ): Result<List<SubtaskModel>> = withContext(Dispatchers.IO) {
        return@withContext kotlin.runCatching {
            taskDao.updateSubtasks(
                taskId,
                newSubtask.map { SubtaskEntity(it.name, it.isCompleted) }
            )

            taskDao.getTaskById(taskId).subtasksId.map { SubtaskModel(it.title, it.isCompleted) }
        }
    }

    override suspend fun updateTask(taskModel: TaskModel): Result<Unit> =
        withContext(Dispatchers.IO) {
            return@withContext kotlin.runCatching {
                taskDao.updateTask(
                    TaskEntity(
                        taskId = taskModel.taskId,
                        title = taskModel.title,
                        description = taskModel.description,
                        deadline = taskModel.deadline,
                        subtasksId = taskModel.subtasks.map {
                            SubtaskEntity(title = it.name, it.isCompleted)
                        },
                        isCompleted = taskModel.isCompleted
                    )
                )

                taskDao.getTaskById(taskModel.taskId).also { entity ->
                    entity.deadline?.let {
                        alarmScheduler.cancel(taskModel)
                        alarmScheduler.schedule(taskModel.copy(taskId = entity.taskId), it)
                    }
                }

                Unit
            }
        }
}