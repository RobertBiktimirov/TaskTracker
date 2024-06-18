package dev.robbik.personalnotes.feature.taskEdit.presentation.edit

import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.core.ui.state.UiState
import dev.robbik.personalnotes.core.ui.state.doIfSuccess

data class TaskStateScreen(
    val state: UiState<TaskModel>,
    var newState: TaskModel?
) {
    val isUpdatedState: Boolean
        get() {
            state.doIfSuccess {
                return it != newState
            }
            return false
        }

    companion object {
        val Init: TaskStateScreen = TaskStateScreen(UiState.Init, null)
    }
}
