package dev.robbik.personalnotes.feature.main.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.robbik.personalnotes.core.di.ViewModelBuilderModule
import dev.robbik.personalnotes.core.di.ViewModelKey
import dev.robbik.personalnotes.feature.main.data.MainRepositoryImpl
import dev.robbik.personalnotes.feature.main.domain.repository.MainRepository
import dev.robbik.personalnotes.feature.main.presentation.MainViewModel

@Module(includes = [ViewModelBuilderModule::class])
internal interface MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    fun bindMainRepository(impl: MainRepositoryImpl): MainRepository
}