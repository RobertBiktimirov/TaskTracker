package dev.robbik.personalnotes.feature.calendar.di

import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModel
import dagger.Component
import dev.robbik.personalnotes.core.database.TaskDao
import dev.robbik.personalnotes.feature.calendar.ui.CalendarFragment
import kotlin.properties.Delegates

@Component(
    dependencies = [CalendarDependencies::class],
    modules = [CalendarModule::class]
)
interface CalendarComponent {

    fun inject(calendarFragment: CalendarFragment)

    @Component.Factory
    interface Factory {
        fun create(dependencies: CalendarDependencies): CalendarComponent
    }
}

interface CalendarDependencies {
    val taskDao: TaskDao
}

interface CalendarDependenciesProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val dependencies: CalendarDependencies

    companion object : CalendarDependenciesProvider by CalendarDependenciesStore
}

object CalendarDependenciesStore : CalendarDependenciesProvider {
    override var dependencies: CalendarDependencies by Delegates.notNull()
}

internal class CalendarComponentStorage : ViewModel() {
    val component =
        DaggerCalendarComponent.factory().create(CalendarDependenciesProvider.dependencies)
}