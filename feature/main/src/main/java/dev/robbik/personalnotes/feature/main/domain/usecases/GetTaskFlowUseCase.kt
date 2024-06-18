package dev.robbik.personalnotes.feature.main.domain.usecases

import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.feature.main.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskFlowUseCase @Inject constructor(private val repository: MainRepository) {
    operator fun invoke(): Flow<List<TaskModel>> = repository.getTasksFlow()
}