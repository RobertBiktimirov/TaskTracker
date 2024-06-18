package dev.robbik.personalnotes.feature.taskEdit.di

import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModel
import dagger.Component
import dev.robbik.personalnotes.core.database.TaskDao
import dev.robbik.personalnotes.core.notification.AlarmScheduler
import dev.robbik.personalnotes.core.task.domain.model.TaskModel
import dev.robbik.personalnotes.feature.taskEdit.presentation.add.TaskAddBottomSheetFragment
import dev.robbik.personalnotes.feature.taskEdit.presentation.add.TaskAddFragment
import dev.robbik.personalnotes.feature.taskEdit.presentation.edit.TaskEditFragment
import kotlin.properties.Delegates

@Component(
    modules = [TaskEditModule::class],
    dependencies = [TaskEditDependencies::class],
)
interface TaskEditComponent {

    fun inject(previewFragment: TaskEditFragment)
    fun inject(addTaskFragment: TaskAddFragment)
    fun inject(taskAddBottomSheetFragment: TaskAddBottomSheetFragment)

    @Component.Factory
    interface Factory {
        fun create(dependencies: TaskEditDependencies): TaskEditComponent
    }
}

interface TaskEditDependencies {
    val taskDao: TaskDao
    val alarmScheduler: AlarmScheduler<TaskModel>
}

interface TaskEditDependenciesProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val dependencies: TaskEditDependencies

    companion object : TaskEditDependenciesProvider by TaskEditDependenciesStore
}

object TaskEditDependenciesStore : TaskEditDependenciesProvider {
    override var dependencies: TaskEditDependencies by Delegates.notNull()
}

internal class TaskEditComponentStorage : ViewModel() {
    val component =
        DaggerTaskEditComponent.factory().create(TaskEditDependenciesProvider.dependencies)
}