package dev.robbik.personalnotes.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.robbik.personalnotes.R
import dev.robbik.personalnotes.core.task.utils.constans.DEFAULT_TASK_ID_PARAM
import dev.robbik.personalnotes.core.task.utils.constans.TASK_PARAM_ID
import dev.robbik.personalnotes.feature.taskEdit.presentation.add.TaskAddBottomSheetFragment
import dev.robbik.personalnotes.feature.main.R.id.mainFragment as MainFragment
import dev.robbik.personalnotes.feature.taskEdit.R.id.previewFragment as TaskPreviewFragment
import dev.robbik.personalnotes.feature.taskEdit.R.id.taskAddFragment as AddTaskFragment
import dev.robbik.personalnotes.feature.calendar.R.id.calendarFragment as CalendarFragment

class MainActivity : AppCompatActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { _: Boolean -> }

    private val bottomNav: BottomNavigationView by lazy {
        findViewById(R.id.bottom_navigation)
    }

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
    }

    private val navController by lazy { navHostFragment.navController }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        askNotificationPermission()
        setupBottomNav()
    }

    private fun setupBottomNav() {
        bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                MainFragment -> showBottomNav()
                TaskPreviewFragment -> hideBottomNav()
                AddTaskFragment -> showBottomNav()
                CalendarFragment -> showBottomNav()
            }
        }

        bottomNav.setOnItemSelectedListener { item: MenuItem ->

            when (item.itemId) {
                R.id.add_section -> {
                    TaskAddBottomSheetFragment().show(supportFragmentManager, "")

                    return@setOnItemSelectedListener false
                }

                R.id.home_section -> {
                    navController.popBackStack()
                    navController.navigate(dev.robbik.personalnotes.feature.main.R.id.feature_main_navigation)
                }

                R.id.calendar_section -> {
                    navController.popBackStack()
                    navController.navigate(dev.robbik.personalnotes.feature.calendar.R.id.feature_calendar_navigation)
                }

            }
            return@setOnItemSelectedListener true
        }

    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isNotificationPermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!isNotificationPermissionGranted) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun showBottomNav() {
        bottomNav.isVisible = true
    }

    private fun hideBottomNav() {
        bottomNav.isVisible = false
    }

}