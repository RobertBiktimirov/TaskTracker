package dev.robbik.personalnotes.feature.taskEdit.domain.usecase

import dev.robbik.personalnotes.feature.taskEdit.domain.TaskEditRepository
import javax.inject.Inject

internal class DeleteTaskUseCase @Inject constructor(
    private val taskEditRepository: TaskEditRepository
) {
    suspend operator fun invoke(taskId: Int): Result<Unit> {
        return taskEditRepository.deleteTask(taskId)
    }
}