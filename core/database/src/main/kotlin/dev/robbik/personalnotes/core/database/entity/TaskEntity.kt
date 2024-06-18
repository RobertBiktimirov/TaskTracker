package dev.robbik.personalnotes.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import dev.robbik.personalnotes.core.database.converter.SubtasksIdConverter
import dev.robbik.personalnotes.core.database.converter.TaskLifecycleConverter
import kotlinx.serialization.Serializable

@Entity(tableName = "task_entity")
data class TaskEntity(
    @ColumnInfo("title") val title: String,

    @ColumnInfo("description") val description: String,
    @ColumnInfo("deadline") val deadline: Long?,
    @TypeConverters(SubtasksIdConverter::class)
    @ColumnInfo("subtasks") var subtasksId: List<SubtaskEntity>,

    @ColumnInfo("is_completed") var isCompleted: Boolean,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("taskId") val taskId: Int = 0
)

@Serializable
data class SubtaskEntity(
    val title: String,
    val isCompleted: Boolean
)

enum class TaskLifecycle {
    NEW,
    PROCESS,
    COMPLETE
}