package dev.robbik.personalnotes.feature.taskEdit.domain.usecase

import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.feature.taskEdit.domain.TaskEditRepository
import javax.inject.Inject

internal class GetTaskByIdUseCase @Inject constructor(
    private val taskEditRepository: TaskEditRepository
) {

    suspend operator fun invoke(taskId: Int): Result<TaskModel> {
        return taskEditRepository.getTaskById(taskId)
    }

}