package dev.robbik.personalnotes.feature.taskEdit.presentation.edit

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.robbik.personalnotes.core.task.domain.model.SubtaskModel
import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.core.ui.state.UiState
import dev.robbik.personalnotes.core.ui.state.doIfSuccess
import dev.robbik.personalnotes.core.ui.string.emptyString
import dev.robbik.personalnotes.core.ui.time.fullFormat
import dev.robbik.personalnotes.feature.taskEdit.domain.models.DeadlineModel
import dev.robbik.personalnotes.feature.taskEdit.domain.models.TimeModel
import dev.robbik.personalnotes.feature.taskEdit.domain.usecase.DeleteTaskUseCase
import dev.robbik.personalnotes.feature.taskEdit.domain.usecase.GetTaskByIdUseCase
import dev.robbik.personalnotes.feature.taskEdit.domain.usecase.UpdateTaskUseCase
import dev.robbik.personalnotes.feature.taskEdit.presentation.utils.TaskEditNoTitleThrowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

internal class TaskEditViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
) : ViewModel() {

    private val _taskStateScreen = MutableStateFlow(TaskStateScreen.Init)
    val taskStateScreen = _taskStateScreen.asStateFlow()

    private val _deleteTaskState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(UiState.Init)
    val deleteTaskState = _deleteTaskState.asStateFlow()

    private val _onBackState = MutableStateFlow<UiState<Boolean>>(UiState.Init)
    val onBackState = _onBackState.asStateFlow()

    private val _deadlineModel = MutableStateFlow(DeadlineModel.Init)
    val deadline = _deadlineModel.asStateFlow()

    private val _errorMessageFlow = MutableStateFlow(emptyString)
    val errorMessageFlow = _errorMessageFlow.asStateFlow()

    private val _exitFlow =MutableStateFlow<Unit?>(null)
    val exitFlow = _exitFlow.asStateFlow()

    init {
        viewModelScope.launch {
            _deadlineModel
                .onEach { deadlineModel ->
                    changeDeadline(deadlineModel.currentDateInMillis)
                }
                .flowOn(Dispatchers.Default)
                .launchIn(viewModelScope)
        }
    }

    fun setTime(hour: Int, minutes: Int) = viewModelScope.launch {
        _deadlineModel.update {
            it.copy(time = TimeModel(hour, minutes))
        }
    }

    fun setDate(timeInMillis: Long) = viewModelScope.launch {
        _deadlineModel.update {
            it.copy(day = timeInMillis)
        }
    }

    private fun Long?.setDeadlineModel() = viewModelScope.launch {
        this@setDeadlineModel ?: return@launch
        val calendar = Calendar.getInstance()
        calendar.time = Date(this@setDeadlineModel)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        _deadlineModel.emit(DeadlineModel(TimeModel(hour, minutes), calendar.timeInMillis))
    }

    fun fetchTask(taskId: Int) = viewModelScope.launch {
        getTaskByIdUseCase(taskId).fold(
            onSuccess = { value: TaskModel ->
                value.deadline.setDeadlineModel()
                _taskStateScreen.emit(
                    TaskStateScreen(state = UiState.Success(value), value)
                )
            },
            onFailure = { exception: Throwable ->
                _taskStateScreen.emit(
                    TaskStateScreen(state = UiState.Failure(exception), null)
                )
            }
        )
    }

    fun changeTitle(title: String) = viewModelScope.launch {
        taskStateScreen.value.state.doIfSuccess { taskModel ->
            val newState = _taskStateScreen.value.newState
            _taskStateScreen.value.newState = (newState ?: taskModel).copy(title = title)
        }
    }

    fun changeDescription(description: String) = viewModelScope.launch {
        taskStateScreen.value.state.doIfSuccess { taskModel ->
            val newState = _taskStateScreen.value.newState
            _taskStateScreen.value.newState =
                (newState ?: taskModel).copy(description = description)
        }
    }

    private fun changeDeadline(time: Long) = viewModelScope.launch {
        taskStateScreen.value.state.doIfSuccess { taskModel ->
            val newState = _taskStateScreen.value.newState

            _taskStateScreen.update {
                it.copy(
                    newState = (newState ?: taskModel).copy(deadline = time)
                )
            }
        }
    }

    fun addSubtask() = viewModelScope.launch {
        _taskStateScreen.update {
            val state = it
            state.state.doIfSuccess {
                val newSubtask = buildList {
                    addAll(state.newState?.subtasks ?: emptyList())
                    add(SubtaskModel("", false))
                }

                return@update TaskStateScreen(
                    state = state.state,
                    newState = state.newState?.copy(subtasks = newSubtask)
                )
            }
            state
        }
    }

    fun changeIsCompletedSubtask(subtaskModel: SubtaskModel): Boolean {
        var isCompleted = false
        taskStateScreen.value.state.doIfSuccess { taskModel ->
            val newState = _taskStateScreen.value.newState
            val newSubtasks = (newState ?: taskModel).subtasks.map {
                if (it == subtaskModel) {
                    isCompleted = it.isCompleted.not()
                    it.copy(
                        isCompleted = it.isCompleted.not()
                    )
                } else it
            }
            _taskStateScreen.update {
                TaskStateScreen(
                    state = UiState.Success(taskModel),
                    newState = newState?.copy(subtasks = newSubtasks)
                )
            }
        }
        return isCompleted
    }

    fun deleteSubtask(task: SubtaskModel): Int? {
        var index: Int? = null

        _taskStateScreen.update {
            val state = it
            state.state.doIfSuccess {
                val subtasks = state.newState?.subtasks?.toMutableList() ?: return@doIfSuccess
                for (i in 0 until subtasks.size) {
                    if (task == subtasks[i]) {
                        index = i
                        subtasks.removeAt(i)
                        break
                    }
                }
                return@update TaskStateScreen(
                    state = state.state,
                    newState = state.newState?.copy(subtasks = subtasks)
                )
            }

            state
        }
        return index
    }

    fun changeTextSubtask(text: String, subtaskModel: SubtaskModel) {
        _taskStateScreen.value.state.doIfSuccess {
            val newState = _taskStateScreen.value.newState
            val newSubtask = newState?.subtasks?.onEach {
                if (it == subtaskModel) it.name = text
            }
            _taskStateScreen.value.newState = newState?.copy(subtasks = newSubtask ?: emptyList())
        }
    }

    fun deleteTask() = viewModelScope.launch {
        _taskStateScreen.value.state.doIfSuccess {
            deleteTaskUseCase(it.taskId).fold(onSuccess = {
                _deleteTaskState.update { UiState.Success(Unit) }
            }, onFailure = {
                _deleteTaskState.update { _ -> UiState.Failure(it) }
            })
        }
    }

    fun handleOnBackButton() = viewModelScope.launch {
        _taskStateScreen.value.state.doIfSuccess {
            return@launch if (_taskStateScreen.value.isUpdatedState) {
                _onBackState.update { UiState.Success(true) }
            } else {
                _onBackState.update { UiState.Success(false) }
            }
        }
        _onBackState.update { UiState.Failure(Throwable()) }
    }

    fun saveNewTaskState() = viewModelScope.launch {
        _taskStateScreen.value.newState?.let {
            updateTaskUseCase(it).fold(
                onSuccess = { _exitFlow.emit(Unit) },
                onFailure = { throwable ->
                    if (throwable is TaskEditNoTitleThrowable) {
                        _errorMessageFlow.emit("Заполните заголовок задачи")
                    }
                }
            )
        }
    }
}