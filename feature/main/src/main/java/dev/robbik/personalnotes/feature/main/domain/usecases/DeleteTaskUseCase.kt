package dev.robbik.personalnotes.feature.main.domain.usecases

import dev.robbik.personalnotes.feature.main.domain.repository.MainRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val repository: MainRepository) {
    suspend operator fun invoke(taskId: Int): Result<Unit> = repository.deleteTask(taskId)
}