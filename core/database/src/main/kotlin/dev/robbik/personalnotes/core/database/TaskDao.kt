package dev.robbik.personalnotes.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.robbik.personalnotes.core.database.entity.SubtaskEntity
import dev.robbik.personalnotes.core.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("select * from task_entity order by deadline asc")
    fun getTaskList(): Flow<List<TaskEntity>>

    @Query("select * from task_entity where taskId = :taskId")
    suspend fun getTaskById(taskId: Int): TaskEntity

    @Update(entity = TaskEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTask(taskEntity: TaskEntity)

    @Query("update task_entity set subtasks = :newSubtask where taskId = :taskId")
    suspend fun updateSubtasks(taskId: Int, newSubtask: List<SubtaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTask(taskEntity: TaskEntity)

    @Query("SELECT * FROM task_entity ORDER BY taskId DESC LIMIT 1")
    suspend fun getLastNewTask(): TaskEntity

    @Query("delete from task_entity where taskId = :taskId")
    suspend fun deleteTask(taskId: Int)

    @Query("select * from task_entity where deadline >= :startTime and deadline < :endTime")
    suspend fun getTaskInTimeRange(startTime: Long, endTime: Long): List<TaskEntity>

    @Query("update task_entity set is_completed = not is_completed where taskId = :taskId")
    suspend fun changeIsCompletedTask(taskId: Int)

}