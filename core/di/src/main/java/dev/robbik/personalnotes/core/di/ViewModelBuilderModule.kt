package dev.robbik.personalnotes.core.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dev.robbik.personalnotes.core.di.ViewModelFactory

@Module
interface ViewModelBuilderModule {
    @Binds
    fun bindViewModelFactory(
        factory: ViewModelFactory,
    ): ViewModelProvider.Factory
}