package dev.robbik.personalnotes.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dev.robbik.personalnotes.core.database.TaskDao
import dev.robbik.personalnotes.core.database.database.TaskDatabase
import dev.robbik.personalnotes.core.notification.AlarmScheduler
import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.presentation.notification.AlarmSchedulerImpl
import javax.inject.Singleton

@Module
interface AppModule {

    @Binds
    fun bindAlarmScheduler(impl: AlarmSchedulerImpl): AlarmScheduler<TaskModel>

    companion object {

        @[Provides Singleton]
        fun provideAppDatabase(context: Context): TaskDatabase =
            Room.databaseBuilder(
                context,
                TaskDatabase::class.java,
                "task_database.db"
            )
                .fallbackToDestructiveMigration()
                .build()


        @[Provides Singleton]
        fun provideGamesDao(database: TaskDatabase): TaskDao = database.taskDao()
    }
}