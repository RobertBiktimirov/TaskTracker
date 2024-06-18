package dev.robbik.personalnotes.feature.taskEdit.presentation.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.robbik.personalnotes.core.task.domain.model.SubtaskModel
import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.core.ui.state.UiState
import dev.robbik.personalnotes.feature.taskEdit.domain.usecase.SaveTaskUseCase
import dev.robbik.personalnotes.feature.taskEdit.domain.models.DeadlineModel
import dev.robbik.personalnotes.feature.taskEdit.domain.models.TimeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class TaskAddViewModel @Inject constructor(
    private val saveTaskUseCase: SaveTaskUseCase,
) : ViewModel() {

    private val _taskEditState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(UiState.Init)
    val taskEditState = _taskEditState.asStateFlow()

    val title = MutableStateFlow("")
    val description = MutableStateFlow("")

    private val _subtasks: MutableStateFlow<List<SubtaskModel>> =
        MutableStateFlow(mutableListOf())
    val subtasks = _subtasks.asStateFlow()

    private val _deadline = MutableStateFlow(DeadlineModel.Init)
    val deadline = _deadline.asStateFlow()

    private val _noTextError: MutableStateFlow<Unit?> = MutableStateFlow(null)
    val noTextError = _noTextError.asStateFlow()

    fun addSubtask() {
        viewModelScope.launch {
            _subtasks.update {
                return@update mutableListOf<SubtaskModel>().apply {
                    addAll(it)
                    add(SubtaskModel("", false))
                }
            }
        }
    }

    fun changeTextTask(text: String, task: SubtaskModel) {
        _subtasks.update { subtasks ->
            subtasks.firstOrNull { it == task }?.name = text
            subtasks
        }
    }

    fun setTime(hour: Int, minute: Int) = viewModelScope.launch {
        _deadline.update {
            it.copy(
                time = TimeModel(hour, minute)
            )
        }
    }

    fun setDate(timeInMillis: Long) = viewModelScope.launch {
        _deadline.update {
            it.copy(day = timeInMillis)
        }
    }

    fun deleteSubtask(task: SubtaskModel): Int? {
        var index: Int? = null
        _subtasks.update {
            val list = it as MutableList

            for (i in 0 until list.size) {
                if (task == list[i]) {
                    index = i
                    list.removeAt(i)
                    break
                }
            }
            list
        }
        return index
    }

    fun saveProject() = viewModelScope.launch {

        if (title.value.isEmpty()) {
            _noTextError.emit(Unit)
            return@launch
        }

        _taskEditState.update { UiState.Loading }

        val taskModel = TaskModel(
            title = title.value,
            description = description.value,
            deadline = _deadline.value.currentDateInMillis,
            subtasks = subtasks.value.filter { it.name.isNotEmpty() },
            taskId = Int.MIN_VALUE,
            isCompleted = false
        )

        Log.d("ROBBIK", "subtask = ${subtasks.value}")

        saveTaskUseCase(taskModel).fold(
            onSuccess = {
                _taskEditState.update { UiState.Success(Unit) }
            },
            onFailure = { ex ->
                _taskEditState.update { UiState.Failure(ex) }
            }
        )
    }
}