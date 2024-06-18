package dev.robbik.personalnotes.feature.calendar.ui

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dev.robbik.personalnotes.core.di.ViewModelFactory
import dev.robbik.personalnotes.core.navigation.AppNavigationDirections
import dev.robbik.personalnotes.core.task.ui.adapter.TaskAdapter
import dev.robbik.personalnotes.core.ui.state.UiState
import dev.robbik.personalnotes.core.ui.binding.bindingNullError
import dev.robbik.personalnotes.core.ui.time.fullFormat
import dev.robbik.personalnotes.feature.calendar.databinding.FragmentCalendarBinding
import dev.robbik.personalnotes.feature.calendar.di.CalendarComponentStorage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.GregorianCalendar
import javax.inject.Inject

class CalendarFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: CalendarViewModel by viewModels { viewModelFactory }

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding ?: bindingNullError()

    private val taskAdapter: TaskAdapter by lazy {
        TaskAdapter(
            taskOnClickListener = this::taskClickListener,
            deleteTaskClickListener = viewModel::deleteTask,
            changeIsCompleteTaskClickListener = viewModel::changeIsCompletedTask
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        ViewModelProvider(this).get<CalendarComponentStorage>()
            .component
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupUI()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tasksState.collect { state ->
                    when (state) {
                        is UiState.Failure -> {
                            Log.d("ROBBIK", "CalendarFragment taskState failure ${state.throwable}")
                        }

                        UiState.Init -> {}
                        UiState.Loading -> {}
                        is UiState.Success -> {
                            taskAdapter.submitList(state.value)
                            binding.noProject.isVisible = state.value.isEmpty()
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectDay.collectLatest {
                    binding.calendar.setDate(it, true, true)
                }
            }
        }

    }

    private fun setupUI() {
        binding.tasks.adapter = taskAdapter

        binding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            viewModel.setDay(GregorianCalendar(year, month, dayOfMonth).timeInMillis)
        }
    }

    private fun taskClickListener(taskId: Int) {
        findNavController().navigate(AppNavigationDirections.actionGlobalEditTaskNavigation(taskId))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}