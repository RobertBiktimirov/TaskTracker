package dev.robbik.personalnotes.feature.calendar.domain

import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val repository: CalendarRepository){
    suspend operator fun invoke(taskId: Int): Result<Unit> = repository.deleteTasks(taskId)
}