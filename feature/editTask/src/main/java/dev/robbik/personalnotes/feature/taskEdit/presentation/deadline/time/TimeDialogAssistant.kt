package dev.robbik.personalnotes.feature.taskEdit.presentation.deadline.time

import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker

internal interface TimeDialogAssistant {
    fun FragmentManager.show(tag: String, selectBlock: (MaterialTimePicker) -> Unit)
}