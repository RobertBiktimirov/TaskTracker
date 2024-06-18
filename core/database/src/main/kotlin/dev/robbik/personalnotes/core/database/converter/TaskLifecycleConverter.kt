package dev.robbik.personalnotes.core.database.converter

import androidx.room.TypeConverter
import dev.robbik.personalnotes.core.database.entity.TaskLifecycle

class TaskLifecycleConverter {

    @TypeConverter
    fun toTaskLifecycle(value: String): TaskLifecycle = enumValueOf<TaskLifecycle>(value)

    @TypeConverter
    fun toValue(lifecycle: TaskLifecycle): String = lifecycle.name

}