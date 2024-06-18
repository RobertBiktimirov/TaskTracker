package dev.robbik.personalnotes.core.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.robbik.personalnotes.core.database.TaskDao
import dev.robbik.personalnotes.core.database.converter.SubtasksIdConverter
import dev.robbik.personalnotes.core.database.converter.TaskLifecycleConverter
import dev.robbik.personalnotes.core.database.entity.TaskEntity

@TypeConverters(SubtasksIdConverter::class, TaskLifecycleConverter::class)
@Database(entities = [TaskEntity::class], exportSchema = false, version = 2)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}