package dev.robbik.personalnotes.core.database.converter

import androidx.room.TypeConverter
import dev.robbik.personalnotes.core.database.entity.SubtaskEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SubtasksIdConverter {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromList(subtasks: List<SubtaskEntity>): String {
        return json.encodeToString(subtasks)
    }

    @TypeConverter
    fun toList(subtasksString: String): List<SubtaskEntity> {
        return json.decodeFromString(subtasksString)
    }

}