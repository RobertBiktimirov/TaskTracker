package dev.robbik.personalnotes.feature.taskEdit.domain.usecase

import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.feature.taskEdit.domain.TaskEditRepository
import javax.inject.Inject

internal class SaveTaskUseCase @Inject constructor(
    private val taskEditRepository: TaskEditRepository
) {
    suspend operator fun invoke(task: TaskModel): Result<Unit> {
        return taskEditRepository.saveTask(task)
    }
}