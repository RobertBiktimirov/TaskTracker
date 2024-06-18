package dev.robbik.personalnotes.feature.taskEdit.presentation.add

import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dev.robbik.personalnotes.core.di.ViewModelFactory
import dev.robbik.personalnotes.core.ui.state.UiState
import dev.robbik.personalnotes.core.ui.binding.bindingNullError
import dev.robbik.personalnotes.feature.taskEdit.databinding.FragmentTaskAddBinding
import dev.robbik.personalnotes.feature.taskEdit.di.TaskEditComponentStorage
import dev.robbik.personalnotes.feature.taskEdit.presentation.subtask.SubtaskAdapter
import dev.robbik.personalnotes.feature.taskEdit.presentation.subtask.SubtaskViewType
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskAddFragment : Fragment() {

    private var _binding: FragmentTaskAddBinding? = null
    private val binding get() = _binding ?: bindingNullError()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<TaskAddViewModel> { viewModelFactory }

    private val subtaskAdapter: SubtaskAdapter by lazy {
        SubtaskAdapter(
            subTaskViewType = SubtaskViewType.ADD,
            closeClickListener = viewModel::deleteSubtask,
            doAfterTextChanged = viewModel::changeTextTask
        )
    }

    private val dateValidatorMin: CalendarConstraints.DateValidator =
        DateValidatorPointForward.from(
            Calendar.getInstance().timeInMillis - 86400000
        )
    private val dateValidator: CalendarConstraints.DateValidator =
        CompositeDateValidator.allOf(listOf(dateValidatorMin))

    private val constraints: CalendarConstraints =
        CalendarConstraints.Builder()
            .setValidator(dateValidator)
            .build()

    private val calendarDatePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Выберите день")
        .setSelection(Calendar.getInstance().timeInMillis)
        .setCalendarConstraints(constraints)
        .build()

    private val timeDatePicker = MaterialTimePicker.Builder()
        .setTitleText("Выберите время")
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
        .build()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ViewModelProvider(this).get<TaskEditComponentStorage>().component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskAddBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dateContainer.setOnClickListener {
            calendarDatePicker.show(childFragmentManager, DATE_TAG)
        }

        binding.timeContainer.setOnClickListener {
            timeDatePicker.show(childFragmentManager, TIME_TAG)
        }

        binding.subtasks.adapter = subtaskAdapter

        binding.addSubtask.setOnClickListener {
            viewModel.addSubtask()
        }

        binding.saveButton.setOnClickListener {
            viewModel.saveProject()
        }

        binding.titleTask.doAfterTextChanged {
            viewModel.title.value = it.toString()
        }

        binding.taskDescriptionValue.doAfterTextChanged {
            viewModel.description.value = it.toString()
        }

        calendarDatePicker.addOnPositiveButtonClickListener {
            viewModel.setDate(calendarDatePicker.selection ?: Calendar.getInstance().timeInMillis)
        }

        timeDatePicker.addOnPositiveButtonClickListener {
            viewModel.setTime(timeDatePicker.hour, timeDatePicker.minute)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.deadline.collect { deadline ->
                    binding.date.text = deadline.formatDate
                    binding.time.text = deadline.formatTime
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subtasks.collect { subtask ->
                    subtaskAdapter.submitList(subtask)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.taskEditState.collect { state ->
                    binding.progress.isVisible = state == UiState.Loading
                    when (state) {
                        is UiState.Failure -> {
                            Toast.makeText(
                                requireContext(),
                                "Не удалось сохранить проект",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is UiState.Success -> {
                            Toast.makeText(
                                requireContext(),
                                "Сохранено",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val DATE_TAG = "date tag"
        private const val TIME_TAG = "time tag"
    }
}