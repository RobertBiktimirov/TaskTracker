package dev.robbik.personalnotes.feature.taskEdit.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.robbik.personalnotes.core.di.ViewModelBuilderModule
import dev.robbik.personalnotes.core.di.ViewModelKey
import dev.robbik.personalnotes.feature.taskEdit.data.TaskEditRepositoryImpl
import dev.robbik.personalnotes.feature.taskEdit.domain.TaskEditRepository
import dev.robbik.personalnotes.feature.taskEdit.presentation.add.TaskAddViewModel
import dev.robbik.personalnotes.feature.taskEdit.presentation.edit.TaskEditViewModel

@Module(includes = [ViewModelBuilderModule::class])
internal interface TaskEditModule {

    @Binds
    @IntoMap
    @ViewModelKey(TaskAddViewModel::class)
    fun bindTaskAddViewModel(viewModel: TaskAddViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TaskEditViewModel::class)
    fun bindTaskEditViewModel(viewModel: TaskEditViewModel): ViewModel

    @Binds
    fun bindTaskEditRepository(impl: TaskEditRepositoryImpl): TaskEditRepository
}