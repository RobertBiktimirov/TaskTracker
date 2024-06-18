package dev.robbik.personalnotes.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dev.robbik.personalnotes.R
import dev.robbik.personalnotes.di.component.AppComponent
import dev.robbik.personalnotes.di.component.DaggerAppComponent
import dev.robbik.personalnotes.feature.calendar.di.CalendarDependenciesStore
import dev.robbik.personalnotes.feature.main.di.MainDependenciesStore
import dev.robbik.personalnotes.feature.taskEdit.di.TaskEditDependenciesStore
import dev.robbik.personalnotes.receiver.CHANNEL_ID

const val CHANNEL_NAME = "Task Tracker Notification"

class PersonalNotesApplication : Application() {

    private var _appComponent: AppComponent? = null
    val appComponent: AppComponent
        get() = requireNotNull(_appComponent) {
            "AppComponent must be not null"
        }

    override fun onCreate() {
        super.onCreate()

        _appComponent = DaggerAppComponent.factory().create(this)

        MainDependenciesStore.dependencies = appComponent
        TaskEditDependenciesStore.dependencies = appComponent
        CalendarDependenciesStore.dependencies = appComponent

        val mChannel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        mChannel.description = getString(R.string.notification_description)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

}

val Context.appComponent: AppComponent
    get() = when (this) {
        is PersonalNotesApplication -> appComponent
        else -> (applicationContext as PersonalNotesApplication).appComponent
    }