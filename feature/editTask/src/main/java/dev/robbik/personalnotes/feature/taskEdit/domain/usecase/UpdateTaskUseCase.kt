package dev.robbik.personalnotes.feature.taskEdit.domain.usecase

import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.feature.taskEdit.domain.TaskEditRepository
import dev.robbik.personalnotes.feature.taskEdit.presentation.utils.TaskEditNoTitleThrowable
import javax.inject.Inject

internal class UpdateTaskUseCase @Inject constructor(
    private val repository: TaskEditRepository
) {
    suspend operator fun invoke(taskModel: TaskModel): Result<Unit> {
        val subtasks = taskModel.subtasks.filter { it.name.isNotEmpty() }
        return checkTitleTask(taskModel.title) {
            repository.updateTask(taskModel.copy(subtasks = subtasks))
        }
    }

    private inline fun checkTitleTask(title: String, block: () -> Result<Unit>): Result<Unit> =
        if (title.isNotEmpty()) block() else Result.failure(TaskEditNoTitleThrowable())
}