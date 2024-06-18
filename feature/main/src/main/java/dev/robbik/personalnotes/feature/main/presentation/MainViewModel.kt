package dev.robbik.personalnotes.feature.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.feature.main.domain.models.TaskCounterModel
import dev.robbik.personalnotes.feature.main.domain.usecases.ChangeIsCompletedTaskUseCase
import dev.robbik.personalnotes.feature.main.domain.usecases.DeleteTaskUseCase
import dev.robbik.personalnotes.feature.main.domain.usecases.GetTaskFlowUseCase
import dev.robbik.personalnotes.feature.main.presentation.filter.FilterModel
import dev.robbik.personalnotes.feature.main.presentation.utlis.withFilterTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class MainViewModel @Inject constructor(
    private val changeIsCompletedTaskUseCase: ChangeIsCompletedTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    getTaskFlowUseCase: GetTaskFlowUseCase
) : ViewModel() {

    private val _tasksFlow: StateFlow<List<TaskModel>> =
        getTaskFlowUseCase().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _tasksUi: MutableStateFlow<List<TaskModel>> = MutableStateFlow(emptyList())
    val tasksUi = _tasksUi.asStateFlow()

    private val _taskCounter = MutableStateFlow(TaskCounterModel())
    val taskCounter = _taskCounter.asStateFlow()

    private val _filters = MutableStateFlow(FilterModel.getInitList())
    val filters = _filters.asStateFlow()

    init {
        fetchTasks()
    }

    fun deleteTask(taskId: Int) = viewModelScope.launch {
        deleteTaskUseCase(taskId)
    }

    fun changeIsCompletedTask(taskId: Int) = viewModelScope.launch {
        changeIsCompletedTaskUseCase(taskId)
    }

    fun changeFilter(filterModel: FilterModel) = viewModelScope.launch {
        _filters.update { filters ->
            filters.map {
                it.copy(isSelected = it.type == filterModel.type)
            }
        }

        _tasksUi.update { tasksModel ->
            val selectedFilter =
                filters.value.find { it.isSelected } ?: return@update tasksModel
            _tasksFlow.value.withFilterTask(selectedFilter)
        }
    }

    private fun fetchTasks() = viewModelScope.launch {
        _tasksFlow.collectLatest { tasks ->
            _tasksUi.update { tasksModel ->
                val selectedFilter =
                    filters.value.find { it.isSelected } ?: return@update tasksModel
                tasks.withFilterTask(selectedFilter)
            }

            _taskCounter.update { counterModel ->
                counterModel.copy(
                    allTasksCount = tasks.size,
                    doneTasksCount = tasks.filter { it.isCompleted }.size
                )
            }
        }
    }
}