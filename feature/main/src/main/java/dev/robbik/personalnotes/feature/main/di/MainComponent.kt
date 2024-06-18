package dev.robbik.personalnotes.feature.main.di

import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModel
import dagger.Component
import dev.robbik.personalnotes.core.database.TaskDao
import dev.robbik.personalnotes.feature.main.presentation.MainFragment
import kotlin.properties.Delegates

@Component(
    dependencies = [MainDependencies::class],
    modules = [MainModule::class]
)
interface MainComponent {

    fun inject(mainFragment: MainFragment)

    @Component.Factory
    interface Factory {
        fun create(dependencies: MainDependencies): MainComponent
    }
}

interface MainDependencies {
    val taskDao: TaskDao
}

interface MainDependenciesProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val dependencies: MainDependencies

    companion object : MainDependenciesProvider by MainDependenciesStore
}

object MainDependenciesStore : MainDependenciesProvider {
    override var dependencies: MainDependencies by Delegates.notNull()
}

internal class MainComponentStorage : ViewModel() {
    val component = DaggerMainComponent.factory().create(MainDependenciesProvider.dependencies)
}