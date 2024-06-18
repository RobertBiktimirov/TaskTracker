package dev.robbik.personalnotes.feature.main.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dev.robbik.personalnotes.core.di.ViewModelFactory
import dev.robbik.personalnotes.core.navigation.AppNavigationDirections
import dev.robbik.personalnotes.core.ui.binding.bindingNullError
import dev.robbik.personalnotes.feature.main.databinding.FragmentMainBinding
import dev.robbik.personalnotes.feature.main.di.MainComponentStorage
import dev.robbik.personalnotes.core.task.ui.adapter.TaskAdapter
import dev.robbik.personalnotes.core.task.utils.constans.DEFAULT_TASK_ID_PARAM
import dev.robbik.personalnotes.core.task.utils.constans.TASK_PARAM_ID
import dev.robbik.personalnotes.feature.main.presentation.adapter.filters.FiltersAdapter
import dev.robbik.personalnotes.feature.main.presentation.filter.FilterModel
import dev.robbik.personalnotes.feature.main.presentation.filter.FilterType
import dev.robbik.personalnotes.core.ui.R as CoreUiR
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding ?: bindingNullError()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    private val taskAdapter: TaskAdapter by lazy {
        TaskAdapter(
            taskOnClickListener = ::toTaskEdit,
            deleteTaskClickListener = viewModel::deleteTask,
            changeIsCompleteTaskClickListener = viewModel::changeIsCompletedTask
        )
    }

    private val filterAdapter: FiltersAdapter by lazy {
        FiltersAdapter(viewModel::changeFilter)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ViewModelProvider(this).get<MainComponentStorage>().component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.intent?.getIntExtra(TASK_PARAM_ID, DEFAULT_TASK_ID_PARAM)?.let {
            if (it != DEFAULT_TASK_ID_PARAM) {
                toTaskEdit(it)
                activity?.intent?.removeExtra(TASK_PARAM_ID)
            }
        }

        binding.tasks.adapter = taskAdapter
        binding.allTasks.title.text = getString(CoreUiR.string.all_tasks_title)
        binding.doneCount.title.text = getString(CoreUiR.string.done_tasks_title)

        binding.filterTasks.adapter = filterAdapter

        binding.doneCount.root.setOnClickListener {
            viewModel.changeFilter(FilterModel(FilterType.COMPLETED, true))
        }

        binding.allTasks.root.setOnClickListener {
            viewModel.changeFilter(FilterModel(FilterType.ALL, true))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.taskCounter.collect {
                    binding.allTasks.value.text = it.allTasksCountUi
                    binding.doneCount.value.text = it.doneTasksCountUi
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filters.collect {
                    binding.filterTasks.smoothScrollToPosition(it.indexOfFirst { it.isSelected })
                    filterAdapter.submitList(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tasksUi.collect {
                    binding.noProjectMessage.isVisible = it.isEmpty()
                    binding.tasks.isVisible = !binding.noProjectMessage.isVisible
                    taskAdapter.submitList(it)
                }
            }
        }
    }

    private fun toTaskEdit(taskId: Int) = findNavController().navigate(
        AppNavigationDirections.actionGlobalEditTaskNavigation(
            taskId
        )
    )

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}