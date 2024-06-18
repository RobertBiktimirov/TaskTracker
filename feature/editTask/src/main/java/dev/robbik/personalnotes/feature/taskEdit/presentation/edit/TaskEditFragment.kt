package dev.robbik.personalnotes.feature.taskEdit.presentation.edit

import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dev.robbik.personalnotes.core.di.ViewModelFactory
import dev.robbik.personalnotes.core.task.domain.model.SubtaskModel
import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.core.ui.state.UiState
import dev.robbik.personalnotes.core.ui.binding.bindingNullError
import dev.robbik.personalnotes.core.ui.fragment.BaseFragment
import dev.robbik.personalnotes.core.ui.time.formatDate
import dev.robbik.personalnotes.core.ui.time.formatWatchs
import dev.robbik.personalnotes.feature.taskEdit.R
import dev.robbik.personalnotes.feature.taskEdit.databinding.FragmentTaskPreviewBinding
import dev.robbik.personalnotes.feature.taskEdit.di.TaskEditComponentStorage
import dev.robbik.personalnotes.feature.taskEdit.presentation.subtask.SubtaskAdapter
import dev.robbik.personalnotes.feature.taskEdit.presentation.subtask.SubtaskViewType
import dev.robbik.personalnotes.feature.taskEdit.presentation.utils.TASK_ID
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskEditFragment : BaseFragment() {

    private var _binding: FragmentTaskPreviewBinding? = null
    private val binding get() = _binding ?: bindingNullError()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<TaskEditViewModel> { viewModelFactory }

    private val subtaskAdapter: SubtaskAdapter by lazy {
        SubtaskAdapter(
            subTaskViewType = SubtaskViewType.EDIT,
            closeClickListener = { task: SubtaskModel -> viewModel.deleteSubtask(task) },
            isCompletedClickListener = viewModel::changeIsCompletedSubtask,
            doAfterTextChanged = viewModel::changeTextSubtask
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
        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .build()


    private val deleteDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Уверены, что хотите удалить проект?")
            .setPositiveButton(getString(R.string.feature_edittask_delete)) { _, _ -> viewModel.deleteTask() }
            .setNegativeButton(getString(R.string.feature_edittask_cancel)) { _, _ -> }
            .create()
    }


    private val onBackDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Сохранить изменения?")
            .setCancelable(false)
            .setPositiveButton("Да") { _, _ ->
                viewModel.saveNewTaskState()
            }
            .setNegativeButton("Нет") { _, _ -> onBackScreen() }
            .create()
    }

    private val taskId: Int by lazy {
        arguments?.getInt(TASK_ID) ?: Int.MIN_VALUE
    }

    private val handleOnBackPressed = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.handleOnBackButton()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ViewModelProvider(this).get<TaskEditComponentStorage>()
            .component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            handleOnBackPressed
        )

        binding.saveButton.setOnClickListener {
            viewModel.saveNewTaskState()
        }

        viewModel.fetchTask(taskId)

        binding.subtasks.adapter = subtaskAdapter

        binding.onBackButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.titleProject.doAfterTextChanged {
            viewModel.changeTitle(it.toString())
        }

        binding.taskDescriptionValue.doAfterTextChanged {
            viewModel.changeDescription(it.toString())
        }

        binding.time.setOnClickListener {
            timeDatePicker.show(childFragmentManager, null)
        }

        binding.day.setOnClickListener {
            calendarDatePicker.show(childFragmentManager, null)
        }

        binding.addSubtask.setOnClickListener {
            viewModel.addSubtask()
        }

        binding.deleteButton.setOnClickListener {
            deleteDialog.show()
        }

        timeDatePicker.addOnPositiveButtonClickListener {
            viewModel.setTime(timeDatePicker.hour, timeDatePicker.minute)
        }

        calendarDatePicker.addOnPositiveButtonClickListener {
            calendarDatePicker.selection?.let {
                viewModel.setDate(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.errorMessageFlow.collect {
                    if (it.isNotEmpty()) showToast(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.exitFlow.collect {
                    it?.let { onBackScreen() }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.onBackState.collect {
                    when (it) {
                        is UiState.Failure -> showToast("Не получилось сохранить изменения")
                        is UiState.Success -> {
                            if (it.value) onBackDialog.show()
                            else findNavController().navigateUp()
                        }

                        else -> {}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.deleteTaskState.collect {
                    when (it) {
                        is UiState.Failure -> {
                            showToast("Не удалось удалить")
                            onBackScreen()
                        }

                        is UiState.Success -> onBackScreen()
                        else -> {}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.taskStateScreen.collect {
                    when (val state = it.state) {
                        is UiState.Failure -> {}
                        is UiState.Success -> {
                            val newState = it.newState
                            binding.setSuccessScreenState(newState ?: state.value)
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun FragmentTaskPreviewBinding.setSuccessScreenState(taskModel: TaskModel) {
        if (titleProject.text.toString() != taskModel.title)
            titleProject.setText(taskModel.title)

        if (taskDescriptionValue.text.toString() != taskModel.description)
            taskDescriptionValue.setText(taskModel.description)

        time.text = taskModel.deadline?.formatWatchs ?: "Нет"
        day.text = taskModel.deadline.formatDate ?: "Нет"
        subtaskAdapter.submitList(taskModel.subtasks)
    }

    private fun onBackScreen() {
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}