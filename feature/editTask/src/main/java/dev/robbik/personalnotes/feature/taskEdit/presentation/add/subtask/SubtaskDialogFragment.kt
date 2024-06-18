package dev.robbik.personalnotes.feature.taskEdit.presentation.add.subtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import dev.robbik.personalnotes.core.ui.binding.bindingNullError
import dev.robbik.personalnotes.feature.taskEdit.databinding.FragmentDialogSubtaskBinding


internal class SubtaskDialogFragment : DialogFragment() {

    private var _binding: FragmentDialogSubtaskBinding? = null
    private val binding get() = _binding ?: bindingNullError()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogSubtaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addSubtask.setOnClickListener {
            if (binding.subtasksTitle.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Задача пустая", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            setFragmentResult(
                RESULT_KEY,
                bundleOf(SUBTASK_KEY to binding.subtasksTitle.text.toString())
            )

            dismiss()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val SUBTASK_KEY = "subtask key"
        const val RESULT_KEY = "add subtask result key"
    }

}