package dev.robbik.personalnotes.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dev.robbik.personalnotes.core.database.TaskDao
import dev.robbik.personalnotes.core.notification.AlarmScheduler
import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.di.modules.AppModule
import dev.robbik.personalnotes.feature.calendar.di.CalendarDependencies
import dev.robbik.personalnotes.feature.main.di.MainDependencies
import dev.robbik.personalnotes.feature.taskEdit.di.TaskEditDependencies
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent : MainDependencies, TaskEditDependencies, CalendarDependencies {

    override val taskDao: TaskDao
    override val alarmScheduler: AlarmScheduler<TaskModel>

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}