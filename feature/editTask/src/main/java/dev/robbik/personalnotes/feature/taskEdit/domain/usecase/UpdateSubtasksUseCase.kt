package dev.robbik.personalnotes.feature.taskEdit.domain.usecase

import dev.robbik.personalnotes.core.task.domain.model.SubtaskModel
import dev.robbik.personalnotes.feature.taskEdit.domain.TaskEditRepository
import javax.inject.Inject

internal class UpdateSubtasksUseCase @Inject constructor(
    private val repository: TaskEditRepository
) {

    suspend operator fun invoke(taskId: Int, newSubtasks: List<SubtaskModel>): Result<List<SubtaskModel>> {
        return repository.updateSubtask(taskId, newSubtasks)
    }
}