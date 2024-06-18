package dev.robbik.personalnotes.feature.taskEdit.presentation.deadline.time

import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import javax.inject.Inject

internal class TimeDialogAssistantImpl @Inject constructor() : TimeDialogAssistant {

    private val timeDatePicker = MaterialTimePicker.Builder()
        .setTitleText("Выберите время")
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .build()

    override fun FragmentManager.show(tag: String, selectBlock: (MaterialTimePicker) -> Unit) {

        timeDatePicker.show(this, tag)
        timeDatePicker.addOnPositiveButtonClickListener {
            selectBlock(timeDatePicker)
        }
    }
}