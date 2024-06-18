package dev.robbik.personalnotes.feature.taskEdit.presentation.deadline.calendar

import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker

internal interface CalendarDialogAssistant {
    fun FragmentManager.show(tag: String, selectBlock: (MaterialTimePicker) -> Unit)
}