package dev.robbik.personalnotes.feature.calendar.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.robbik.personalnotes.core.di.ViewModelBuilderModule
import dev.robbik.personalnotes.core.di.ViewModelKey
import dev.robbik.personalnotes.feature.calendar.data.CalendarRepositoryImpl
import dev.robbik.personalnotes.feature.calendar.domain.CalendarRepository
import dev.robbik.personalnotes.feature.calendar.ui.CalendarViewModel

@Module(includes = [ViewModelBuilderModule::class])
interface CalendarModule {

    @Binds
    @IntoMap
    @ViewModelKey(CalendarViewModel::class)
    fun bindCalendarViewModel(viewModel: CalendarViewModel): ViewModel

    @Binds
    fun bindCalendarRepository(impl: CalendarRepositoryImpl): CalendarRepository
}