package dev.robbik.personalnotes.feature.calendar.domain

import javax.inject.Inject

class ChangeIsCompletedTaskUseCase @Inject constructor(private val repository: CalendarRepository) {
    suspend operator fun invoke(taskId: Int): Result<Unit> =
        repository.changeIsCompletedTask(taskId)
}