package com.example.mychatapp.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mychatapp.R
import com.example.mychatapp.data.db.remote.FirebaseDataSource
import com.example.mychatapp.util.forceHideKeyboard
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var navView: BottomNavigationView
    private lateinit var mainProgressBar: ProgressBar
    private lateinit var mainToolbar: Toolbar
    private lateinit var notificationsBadge: BadgeDrawable
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainToolbar = findViewById(R.id.main_toolbar)
        navView = findViewById(R.id.nav_view)
        mainProgressBar = findViewById(R.id.main_progressBar)

        notificationsBadge =
            navView.getOrCreateBadge(R.id.navigation_notifications).apply { isVisible = false }

        setSupportActionBar(mainToolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.startFragment) {
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
            }

            when (destination.id) {
                R.id.calendarsFragment -> navView.visibility = View.GONE
                R.id.profileFragment -> navView.visibility = View.GONE
                R.id.chatFragment -> navView.visibility = View.GONE
                R.id.startFragment -> navView.visibility = View.GONE
                R.id.loginFragment -> navView.visibility = View.GONE
                R.id.createAccountFragment -> navView.visibility = View.GONE
                else -> navView.visibility = View.VISIBLE
            }
            showGlobalProgressBar(false)
            currentFocus?.rootView?.forceHideKeyboard()
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_calendars,
                R.id.navigation_chats,
                R.id.navigation_notifications,
                R.id.navigation_users,
                R.id.navigation_settings,
                R.id.startFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        FirebaseDataSource.dbInstance.goOnline()
    }

    override fun onPause() {
        super.onPause()
        FirebaseDataSource.dbInstance.goOffline()
    }

    override fun onResume() {
        try {
            setupViewModelObservers()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        super.onResume()
    }

    private fun setupViewModelObservers() {
        viewModel.userNotificationsList.observe(this) {
            if (it != null) {
                if (it.size > 0) {
                    notificationsBadge.number = it.size
                    notificationsBadge.isVisible = true
                } else {
                    notificationsBadge.isVisible = false
                }
            }
        }
    }

    fun showGlobalProgressBar(show: Boolean) {
        if (show) mainProgressBar.visibility = View.VISIBLE
        else mainProgressBar.visibility = View.GONE
    }
}