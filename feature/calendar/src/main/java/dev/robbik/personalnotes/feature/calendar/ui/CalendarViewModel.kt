package dev.robbik.personalnotes.feature.calendar.ui

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.core.ui.state.UiState
import dev.robbik.personalnotes.core.ui.time.startDay
import dev.robbik.personalnotes.feature.calendar.domain.ChangeIsCompletedTaskUseCase
import dev.robbik.personalnotes.feature.calendar.domain.DeleteTaskUseCase
import dev.robbik.personalnotes.feature.calendar.domain.GetTaskByDayFlowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CalendarViewModel @Inject constructor(
    private val changeIsCompletedTaskUseCase: ChangeIsCompletedTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTaskByDayFlowUseCase: GetTaskByDayFlowUseCase,
) : ViewModel() {

    private val _tasksState = MutableStateFlow<UiState<List<TaskModel>>>(UiState.Init)
    val tasksState = _tasksState.asStateFlow()

    private val _selectDay = MutableStateFlow(Calendar.getInstance().timeInMillis.startDay)
    val selectDay = _selectDay.asStateFlow()

    init {
        viewModelScope.launch {
            _selectDay
                .flatMapLatest { fetchDay(it) }
                .onEach { tasks -> _tasksState.update { UiState.Success(tasks) } }
                .flowOn(Dispatchers.Default)
                .launchIn(viewModelScope)

        }
    }

    fun setDay(timeInMillis: Long) = viewModelScope.launch {
        _selectDay.emit(timeInMillis.startDay)
    }

    private fun fetchDay(timeInMillis: Long): Flow<List<TaskModel>> =
        getTaskByDayFlowUseCase(timeInMillis)


    fun deleteTask(taskId: Int) = viewModelScope.launch {
        deleteTaskUseCase(taskId)
    }

    fun changeIsCompletedTask(taskId: Int) = viewModelScope.launch {
        changeIsCompletedTaskUseCase(taskId)
    }
}