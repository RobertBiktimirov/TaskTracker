package dev.robbik.personalnotes.feature.calendar.domain

import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskByDayFlowUseCase @Inject constructor(private val repository: CalendarRepository) {
    operator fun invoke(startTime: Long): Flow<List<TaskModel>> =
        repository.getTaskByDayFlow(startTime)
}